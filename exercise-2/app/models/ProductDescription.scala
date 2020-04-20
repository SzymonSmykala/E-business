package models

import play.api.libs.json.Json

case class ProductDescription(var id: Long, var productId: Long, var description: String)

object ProductDescription{
  implicit val productDescriptionFormat = Json.format[ProductDescription]
}
