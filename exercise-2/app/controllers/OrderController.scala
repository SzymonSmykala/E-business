package controllers

import DTO.Order
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._

import scala.collection.mutable.ListBuffer


@Singleton
class OrderController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[Order] = ListBuffer();
    for (i <- 0 to 10){
      result += Order(i, 1, 2);
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = Order(1, 1,2)
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val order = json.as[Order]
    print(order)
    Ok(Json.toJson(order))
  }

  def add() = Action { request =>
    val json = request.body.asJson.get
    val order = json.as[Order]
    print(order)
    Ok(Json.toJson(order))
  }

  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}
