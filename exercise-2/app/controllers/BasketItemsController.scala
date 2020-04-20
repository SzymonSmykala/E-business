package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import models.{BasketItem, BasketItemRepository}

import scala.concurrent.ExecutionContext


@Singleton
class BasketItemsController @Inject()(cc: ControllerComponents, basketItemRepository: BasketItemRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def readAll = Action.async {
    val result = basketItemRepository.list()
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def get(id: Long) = Action.async {
    val result = basketItemRepository.getById(id);
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val basketItem = json.as[BasketItem]
    val result = basketItemRepository.create(basketItem.id, basketItem.productId, basketItem.count, basketItem.basketId)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def delete(id: Long) = Action.async {
    val result = basketItemRepository.delete(id)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getByBasketId(basket_id: Long) = Action.async{
    val result = basketItemRepository.getBasketItemsByBasketId(basket_id)
    result map {
      r => Ok(Json.toJson(r))
    }
  }
}
