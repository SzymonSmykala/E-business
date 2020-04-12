package controllers

import java.util.Date

import DTO.Promo
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import org.joda.time.DateTime

import scala.collection.mutable.ListBuffer


@Singleton
class PromoController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[Promo] = ListBuffer();
    for (i <- 0 to 10){
      result += Promo(i, 1, DateTime.now(), DateTime.now(), 20);
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = Promo(1, 1, DateTime.now(), DateTime.now(), 20);
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Promo]
    print(product)
    Ok(Json.toJson(product))
  }

  def add() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Promo]
    print(product)
    Ok(Json.toJson(product))
  }

  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}