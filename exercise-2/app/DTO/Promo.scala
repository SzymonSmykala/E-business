package DTO

import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import org.joda.time.DateTime
import play.api.libs.json.Json

case class Promo(var id: Float, var productId: Float, var startDate: DateTime, var endData: DateTime, var looseAmount: Int)

object Promo{
  implicit val promoFormat = Json.format[Promo];
}

