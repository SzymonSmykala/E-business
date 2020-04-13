package controllers

import DTO.{BasketItem}
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._

import scala.collection.mutable.ListBuffer


@Singleton
class BasketItemsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[BasketItem] = ListBuffer();
    for (i <- 0 to 10){
      result += BasketItem(i, 1,2,3);
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = BasketItem(1, 1, 1,1)
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[BasketItem]
    print(product)
    Ok(Json.toJson(product))
  }

  def add() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[BasketItem]
    print(product)
    Ok(Json.toJson(product))
  }

  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}
