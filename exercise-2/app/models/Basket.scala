package models

import play.api.libs.json.Json

case class Basket(var id: Long, var userId: Long)

object Basket{
  implicit val basketFormat = Json.format[Basket]
}
