package models

import play.api.libs.json.Json


case class Product(var name: String, var categoryId: Long, var id: Long)

object Product{
  implicit val productFormat = Json.format[Product];
}