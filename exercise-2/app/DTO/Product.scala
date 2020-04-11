package DTO

import play.api.libs.json.Json


case class Product(var name: String, var categoryId: Int, var id: Int)

object Product{
  implicit val productFormat = Json.format[Product];

}