package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.json._
import javax.inject._
import models.{Order, OrderRepository}

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OrderController @Inject()(cc: ControllerComponents, orderRepository: OrderRepository) (implicit ec: ExecutionContext)extends AbstractController(cc) {

  def get(id: Long) = Action.async {
    val result = orderRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val payment = json.as[Order]
    val updateResult = orderRepository.update(payment.id, payment)
    updateResult map {r => Ok(Json.toJson(payment))}
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val order = json.as[Order]
    val inserted : Future[Order] = orderRepository.create(order.id, order.basketId, order.paymentId)
    inserted map {
      i =>
        Ok(Json.toJson(i))
    }
  }

  def readAll = Action.async { request =>
    val result = orderRepository.list()
    result.map(prod => Ok(Json.toJson(prod)))
  }

  def delete(id: Long) = Action.async{
    val deleteResult = orderRepository.delete(id)
    deleteResult map {
      r => Ok(Json.toJson(r))
    }
  }

}
