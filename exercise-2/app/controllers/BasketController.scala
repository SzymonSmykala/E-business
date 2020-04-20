package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import models.{Basket, BasketRepository}

import scala.concurrent.ExecutionContext


@Singleton
class BasketController @Inject()(cc: ControllerComponents, basketRepository: BasketRepository)(implicit ec: ExecutionContext)  extends AbstractController(cc) {

  def readAll = Action.async {
    val result = basketRepository.list()
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def get(id: Long) = Action.async {
    val result = basketRepository.getById(id);
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val product = json.as[Basket]
    val result = basketRepository.create(product.id, product.userId)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def delete(id: Long) = Action.async {
    val result = basketRepository.delete(id)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getByUserId(user_id: Long) = Action.async{
    val result = basketRepository.getBasketByUserId(user_id)
    result map {
      r => Ok(Json.toJson(r))
    }
  }

}
