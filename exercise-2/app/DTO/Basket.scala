package DTO

import play.api.libs.json.Json

case class Basket(var id: Int)

object Basket{
  implicit val basketFormat = Json.format[Basket]
}
