package models

import play.api.libs.json.Json

case class FavoriteItem(var id: Long, var userId: Long, var productId: Long)

object FavoriteItem{
  implicit val favoriteItemFormat = Json.format[FavoriteItem]
}
