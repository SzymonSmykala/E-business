package controllers

import java.util.concurrent.TimeUnit

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.{Basket, BasketRepository, Order, OrderRepository, Payment, PaymentRepository, Product}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import services.TokenManager

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class OrderController @Inject()(cc: MessagesControllerComponents, orderRepository: OrderRepository, basketRepository: BasketRepository, paymentRepository: PaymentRepository, tokenManger: TokenManager)(implicit ec: ExecutionContext)extends MessagesAbstractController(cc) {

  val orderFormCreate: Form[CreateOrderForm] = Form {
    mapping(
      "basket" -> number,
      "payment" ->number,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  def addOrderForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val payments = paymentRepository.list()
    val baskets = Await.result(basketRepository.list(), Duration(10, TimeUnit.SECONDS));
    payments map {p =>
      Ok(views.html.orderadd(orderFormCreate, baskets, p))
    }
  }

  def addOrderHandler()() = Action.async { implicit request =>
    var pay:Seq[Payment] = Seq[Payment]()
    paymentRepository.list().onComplete{
      case Success(value) => pay = value
      case Failure(_) => print("fail")
    }

    var bask:Seq[Basket] = Seq[Basket]()
    basketRepository.list().onComplete{
      case Success(value) => bask = value
      case Failure(_) => print("fail")
    }

    orderFormCreate.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.orderadd(errorForm, bask, pay))
        )
      },
      order => {
        orderRepository.create(1, order.basket, order.payment).map { _ =>
          Ok("Ok")
        }
      }
    )
  }

  def getOrders: Action[AnyContent] = Action.async { implicit request =>
    val produkty = orderRepository.list()
    produkty.map( products => Ok(views.html.orders(products)))
  }

  def deleteOrder(id: Long): Action[AnyContent] = Action { implicit request =>
    orderRepository.delete(id)
    Redirect(routes.OrderController.getOrders())
  }

  def get(id: Long) = Action.async {
    val result = orderRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val order = json.as[Order]
    val updateResult = orderRepository.update(order.id, order)
    updateResult map {r => Ok(Json.toJson(order))}
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

  def getOrdersByUser = Action {implicit request =>
    val token = request.headers.get("token");
    val user = tokenManger.getUserBy(token.get);
    var baskets = Await.result(basketRepository.getAllBasketsByUser(user), Duration.Inf);
    var orders = Seq[Order]();
    baskets map {
      b => {
        var or = Await.result(orderRepository.getByBasketId(b.id), Duration.Inf);
        orders = orders ++ or
      }

    }
    Ok(Json.toJson(orders))
  }

}

case class UpdateOrderForm(var id: Int, var basket: Int, var payment: Int)
case class CreateOrderForm(var basket: Int, var payment: Int)