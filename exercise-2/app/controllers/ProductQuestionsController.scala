package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import repositories.DTO.ProductQuestion

import scala.collection.mutable.ListBuffer


@Singleton
class ProductQuestionsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[ProductQuestion] = ListBuffer();
    for (i <- 0 to 10){
      result += ProductQuestion(i, 2, "content")
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = ProductQuestion(id, 2, "content")
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[ProductQuestion]
    print(product)
    Ok(Json.toJson(product))
  }

  def add() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[ProductQuestion]
    print(product)
    Ok(Json.toJson(product))
  }

  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}
