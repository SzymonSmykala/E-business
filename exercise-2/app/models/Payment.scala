package models

import play.api.libs.json.{Json, OFormat}

case class Payment (var id: Long, var status: String)

object Payment{
  implicit val paymentFormat: OFormat[Payment] = Json.format[Payment]
}
