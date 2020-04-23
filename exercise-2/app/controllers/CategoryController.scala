package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesRequest, Request}
import play.api.libs.json._
import javax.inject._
import models.{Category, CategoryRepository}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class CategoryController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext, categoryRepository: CategoryRepository)  extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

//  def addCategoryForm(): Action[AnyContent] = Action {
//    Ok(views.html.addcategory(categoryForm))
//  }

//  def addProduct: Action[AnyContent] = Action { implicit request: MessagesRequest[AnyContent] =>
//    val categories = categoryRepository.list()
//    Ok(views.html.addcategory(categoryForm))
//  }
//
//  def addCategoryForm = Action  { implicit request: Request[AnyContent] =>
//    Ok(views.html.addcategory(categoryForm))
//   }


//  def addCategoryFormHandle = Action.async { implicit request =>
//
//    categoryForm.bindFromRequest.fold(
//      errorForm => {
//        Future.successful(
//          BadRequest(views.html.addcategory(errorForm))
//        )
//      },
//      category => {
//        categoryRepository.create(1, category.name).map { _ =>
//          Redirect(routes.CategoryController.get(1)).flashing("success" -> "product.created")
//        }
//      }
//    )
//
//  }

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
