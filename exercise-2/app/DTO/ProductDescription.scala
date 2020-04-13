package DTO

import play.api.libs.json.Json

case class ProductDescription(var productId: Float, var description: String)

object ProductDescription{
  implicit val productDescriptionFormat = Json.format[ProductDescription]
}
