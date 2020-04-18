package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import repositories.DTO.FavoriteItem

import scala.collection.mutable.ListBuffer


@Singleton
class FavoriteItemsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def readAll = Action {
    var result: ListBuffer[FavoriteItem] = ListBuffer();
    for (i <- 0 to 10){
      result += FavoriteItem(i, 1, 2);
    }
    Ok(Json.toJson(result.toList))
  }

  def get(id: Long) = Action {
    var result = FavoriteItem(1, 1,2)
    Ok(Json.toJson(result));
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val order = json.as[FavoriteItem]
    print(order)
    Ok(Json.toJson(order))
  }

  def add() = Action { request =>
    val json = request.body.asJson.get
    val order = json.as[FavoriteItem]
    print(order)
    Ok(Json.toJson(order))
  }

  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}
