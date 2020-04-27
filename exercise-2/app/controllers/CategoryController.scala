package controllers

import javax.inject.{Inject, _}
import models.{Category, CategoryRepository}
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CategoryController @Inject()(cc: MessagesControllerComponents)(implicit ec: ExecutionContext, categoryRepository: CategoryRepository)  extends MessagesAbstractController(cc)  {

  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }
  val categoryFormUpdate: Form[UpdateCategoryForm] = Form {
    mapping(
      "id"  -> number,
      "name" -> nonEmptyText,
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  def getCategories: Action[AnyContent] = Action.async { implicit request =>
    val c = categoryRepository.list()
    c.map( cat => Ok(views.html.categories(cat)))
  }

  def addCategoryForm: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.addcategory(categoryForm))
  }

  def updateCategory(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val category = categoryRepository.getById(id)
    category.map(product => {
      val prodForm = categoryFormUpdate.fill(UpdateCategoryForm(product.id.toInt, product.name))
      Ok(views.html.categoryupdate(prodForm))
    })
  }

  def updateCategoryHandle(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    categoryFormUpdate.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categoryupdate(errorForm))
        )
      },
      category => {
        categoryRepository.update(category.id, Category(category.id, category.name)).map { _ =>
          Redirect(routes.CategoryController.updateCategory(category.id)).flashing("success" -> "category updated")
        }
      }
    )
  }

  def addCategoryFormHandler = Action.async { implicit request =>

    categoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.addcategory(errorForm))
        )
      },
      product => {
        categoryRepository.create(1, product.name).map { _ =>
          Ok("Ok")
        }
      }
    )

  }

  def deleteCategory(id: Long) = Action {implicit request =>
      categoryRepository.delete(id)
      Redirect(routes.CategoryController.getCategories())
  }

  def get(id: Long) = Action.async {
    val result = categoryRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val category = json.as[Category]
    val updateResult = categoryRepository.update(category.id, category)
    updateResult map {r => Ok(Json.toJson(category))}
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val category = json.as[Category]
    val inserted : Future[Category] = categoryRepository.create(category.id, category.name)
    inserted map {
      i =>
        Ok(Json.toJson(i))
    }
  }

  def readAll = Action.async { request =>
    val result = categoryRepository.list()
    result.map(prod => Ok(Json.toJson(prod)))
  }

  def delete(id: Long) = Action.async{
    val deleteResult = categoryRepository.delete(id)
    deleteResult map {
      r => Ok(Json.toJson(r))
    }
  }

}


case class CreateCategoryForm(name: String)
case class UpdateCategoryForm(var id: Int, var name: String)
