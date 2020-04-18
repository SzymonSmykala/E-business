package repositories.DTO

import play.api.libs.json.Json

case class BasketItem(var id: Long, var productId: Long, var count: Int, var basketId: Long)

object BasketItem{
 implicit val basketItemFormat = Json.format[BasketItem];
}
