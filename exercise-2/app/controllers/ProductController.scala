package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.{Category, CategoryRepository, Product, ProductRepository}
import play.api.data.Forms.mapping
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class ProductController @Inject()(productsRepository: ProductRepository,  categoryRepo: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "category" -> number,
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  def addProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepo.list()
    categories.map (cat => Ok(views.html.productadd(productForm, cat)))
  }

  def addProductHandle = Action.async { implicit request =>
    var categ:Seq[Category] = Seq[Category]()
    val categories = categoryRepo.list().onComplete{
      case Success(cat) => categ = cat
      case Failure(_) => print("fail")
    }

    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.productadd(errorForm, categ))
        )
      },
      product => {
        productsRepository.create(product.name, product.category.toLong, 1).map { _ =>
          Ok("Ok")
        }
      }
    )

  }


  def get(id: Long) = Action.async {
    val result = productsRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val product = json.as[Product]
    val updateResult = productsRepository.update(product.id, product)
    updateResult map {r => Ok(Json.toJson(product))}
  }

  def add():Action[AnyContent] = Action.async{ implicit request: MessagesRequest[AnyContent]  =>
      val json = request.body.asJson.get
      val product = json.as[Product]
      val inserted : Future[Product] = productsRepository.create(product.name, product.categoryId, product.id)
      inserted map {
        i =>
          Ok(Json.toJson(i))
      }
  }

  def readAll: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productsRepository.list()
    products.map(prod => Ok(Json.toJson(prod)))
  }

  def delete(id: Long) = Action.async{
    val deleteResult = productsRepository.delete(id)
    deleteResult map {
      r => Ok(Json.toJson(r))
    }
  }
}

case class CreateProductForm(var name: String, var category: Int)
