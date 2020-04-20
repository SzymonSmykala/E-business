package models

import play.api.libs.json.Json

case class ProductQuestion(var id: Long, var productId: Long, var questionContent: String)

object ProductQuestion{
  implicit val productQuestionFormat = Json.format[ProductQuestion]
}