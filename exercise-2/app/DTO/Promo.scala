package DTO

import java.util.Date

import play.api.libs.json.Json

case class Promo(var id: Float, var productId: Float, var startDate: Date, var endData: Date, var looseAmount: Int)

object Promo{
  implicit val promoFormat = Json.format[Promo];
}