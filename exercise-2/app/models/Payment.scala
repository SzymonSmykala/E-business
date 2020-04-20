package models

import play.api.libs.json.Json

case class Payment (var id: Long, var status: String)

object Payment{
  implicit val paymentFormat = Json.format[Payment]
}
