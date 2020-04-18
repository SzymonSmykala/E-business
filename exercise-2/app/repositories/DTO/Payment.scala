package repositories.DTO

import play.api.libs.json.Json

case class Payment (var id: Float, var status: String)

object Payment{
  implicit val paymentFormat = Json.format[Payment]
}
