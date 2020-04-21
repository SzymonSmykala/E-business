package models

import play.api.libs.json.Json

case class Category(var id: Long, var name: String)

object Category {
    implicit val categoryFormat = Json.format[Category]
}
