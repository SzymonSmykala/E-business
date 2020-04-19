package models

import play.api.libs.json.Json

case class ProductQuestion(var id: Float, var productId: Float, var questionContent: String)

object ProductQuestion{
  implicit val productQuestionFormat = Json.format[ProductQuestion]
}