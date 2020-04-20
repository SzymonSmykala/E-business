package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import models.{FavoriteItem, FavoriteItemsRepository}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext


@Singleton
class FavoriteItemsController @Inject()(cc: ControllerComponents, favoriteItemsRepository: FavoriteItemsRepository) (implicit ec: ExecutionContext) extends AbstractController(cc) {


  def readAll = Action.async {
    val result = favoriteItemsRepository.list()
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def get(id: Long) = Action.async {
    val result = favoriteItemsRepository.getById(id);
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val favItem = json.as[FavoriteItem]
    val result = favoriteItemsRepository.create(favItem.id, favItem.userId, favItem.productId)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def delete(id: Long) = Action.async {
    val result = favoriteItemsRepository.delete(id)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getByUserId(user_id: Long) = Action.async{
    val result = favoriteItemsRepository.getFavoriteItemsByUserId(user_id)
    result map {
      r => Ok(Json.toJson(r))
    }
  }
}
