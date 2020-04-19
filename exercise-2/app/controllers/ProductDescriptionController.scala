package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import models.{ProductDescription, ProductDescriptionRepository}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext


@Singleton
class ProductDescriptionController @Inject()(cc: ControllerComponents, productDescriptionRepository: ProductDescriptionRepository)(implicit ec: ExecutionContext)  extends AbstractController(cc) {

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
