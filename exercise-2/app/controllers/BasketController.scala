package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import models.Basket

import scala.collection.mutable.ListBuffer


@Singleton
class BasketController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[Basket] = ListBuffer();
    for (i <- 0 to 10){
      result += Basket(i, 1);
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = Basket(1, 1)
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Basket]
    print(product)
    Ok(Json.toJson(product))
  }

  def add() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Basket]
    print(product)
    Ok(Json.toJson(product))
  }

  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}
