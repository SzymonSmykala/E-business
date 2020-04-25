package controllers

import javax.inject.{Inject, _}
import models.{Product, ProductDescription, ProductDescriptionRepository, ProductRepository}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.libs.json._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class ProductDescriptionController @Inject()(cc: MessagesControllerComponents, productDescriptionRepository: ProductDescriptionRepository, productRepository: ProductRepository)(implicit ec: ExecutionContext)  extends MessagesAbstractController(cc) {

  val productDescriptionCreateForm: Form[ProductDescriptionCreateForm] = Form {
    mapping(
      "product" -> number,
      "description" -> text,
    )(ProductDescriptionCreateForm.apply)(ProductDescriptionCreateForm.unapply)
  }

  def addProductQuestionForm: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productRepository.list()
    products map {p =>
      Ok(views.html.productdescriptionadd(productDescriptionCreateForm, p))
    }
  }

  def addProductQuestionHandle = Action.async { implicit  request =>
    var prod:Seq[Product] = Seq[Product]()
    productRepository.list().onComplete{
      case Success(value) => prod = value
      case Failure(_) => print("fail")
    }
    productDescriptionCreateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.productdescriptionadd(errorForm, prod))
        )
      },
      productDescription => {
        productDescriptionRepository.create(1, productDescription.product, productDescription.description).map { _ =>
          Ok("Ok")
        }
      }
    )
  }


  def readAll: Action[AnyContent] = Action.async {
    val result = productDescriptionRepository.list()
    result map { r => Ok(Json.toJson(r));};
  }

  def get(id: Long) = Action.async {
    val result = productDescriptionRepository.getById(id)
    result map { r => Ok(Json.toJson(r));};
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val product = json.as[ProductDescription]
    val result = productDescriptionRepository.update(product.id, product)
    result map {
      r => Ok(Json.toJson(product))
    }
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val productDescription = json.as[ProductDescription]
    val result = productDescriptionRepository.create(productDescription.id, productDescription.productId, productDescription.description)
    result map {
      r => Ok(Json.toJson(productDescription))
    }
  }

  def delete(id: Long) = Action.async{
    val result = productDescriptionRepository.delete(id)
    result map {
      r => Ok(Json.toJson(r))
    }
  }
}

case class ProductDescriptionCreateForm(var product: Int, var description: String)