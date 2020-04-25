package controllers

import javax.inject.{Inject, _}
import models.{ProductDescription, ProductDescriptionRepository}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.libs.json._
import play.api.data.Form
import play.api.data.Forms._

import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.ExecutionContext


@Singleton
class ProductDescriptionController @Inject()(cc: MessagesControllerComponents, productDescriptionRepository: ProductDescriptionRepository)(implicit ec: ExecutionContext)  extends MessagesAbstractController(cc) {

  val productDescriptionCreateForm: Form[ProductDescriptionCreateForm] = Form {
    mapping(
      "product" -> number,
      "description" -> text,
    )(ProductDescriptionCreateForm.apply)(ProductDescriptionCreateForm.unapply)
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