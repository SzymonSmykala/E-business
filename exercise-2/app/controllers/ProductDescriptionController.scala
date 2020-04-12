package controllers


import DTO.ProductDescription
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._

import scala.collection.mutable.ListBuffer


@Singleton
class ProductDescriptionController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[ProductDescription] = ListBuffer();
    for (i <- 0 to 10){
      result += ProductDescription(i, "content")
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = ProductDescription(id, "content")
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[ProductDescription]
    print(product)
    Ok(Json.toJson(product))
  }

  def add() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[ProductDescription]
    print(product)
    Ok(Json.toJson(product))
  }

  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}