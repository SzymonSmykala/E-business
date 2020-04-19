package models

import play.api.libs.json.Json

case class FavoriteItem(var id: Float, var userId: Float, var productId: Float)

object FavoriteItem{
  implicit val favoriteItemFormat = Json.format[FavoriteItem]
}
