package DTO

import play.api.libs.json.Json

case class Order(var id: Float, var basketId: Float, var paymentId: Float)

object Order{
  implicit val orderFormat = Json.format[Order];
}


