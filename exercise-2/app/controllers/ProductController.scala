package controllers

import DTO.Product
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._

import scala.collection.mutable.ListBuffer


@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[Product] = ListBuffer();
    for (i <- 0 to 10){
        result += Product(s"Product$i ", i + 1, i);
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = Product("name", 1, 1)
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Product]
    print(product)
    Ok(Json.toJson(product))
  }

  def add() = Action{ request =>
      val json = request.body.asJson.get
      val product = json.as[Product]
      print(product)
      Ok(Json.toJson(product))
  }

  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}
