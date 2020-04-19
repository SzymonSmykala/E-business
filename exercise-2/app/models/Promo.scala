package models

import play.api.libs.json.{Json, OFormat}

case class Promo(var id: Float, var productId: Float, var looseAmount: Int)

object Promo{
  implicit val promoFormat: OFormat[Promo] = Json.format[Promo];
}

