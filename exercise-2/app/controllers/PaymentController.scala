package controllers

import DTO.Payment
import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._

import scala.collection.mutable.ListBuffer


@Singleton
class PaymentController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[Payment] = ListBuffer();
    for (i <- 0 to 10){
      result += Payment(i, "Confirmed");
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = Payment(1, "Confirmed")
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Payment]
    print(product)
    Ok(Json.toJson(product))
  }

  def add() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Payment]
    print(product)
    Ok(Json.toJson(product))
  }

}
