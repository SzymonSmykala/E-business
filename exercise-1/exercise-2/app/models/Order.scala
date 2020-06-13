package models

import play.api.libs.json.Json

case class Order(var id: Long, var basketId: Long, var paymentId: Long)

object Order{
  implicit val orderFormat = Json.format[Order];
}


