package controllers

import java.util.concurrent.TimeUnit

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.{Basket, BasketItem, BasketItemRepository, BasketRepository, ProductRepository, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.mapping

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class BasketItemsController @Inject()(cc: MessagesControllerComponents, basketItemRepository: BasketItemRepository, productRepository: ProductRepository, basketRepository: BasketRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val basketItemFormCreate: Form[CreateBasketItemForm] = Form {
    mapping(
      "product" -> number,
      "count" ->number,
      "basket" -> number,
    )(CreateBasketItemForm.apply)(CreateBasketItemForm.unapply)
  }

  def basketItemForm: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
      val products = productRepository.list()
      val baskets = Await.result(basketRepository.list(), Duration(10, TimeUnit.SECONDS));
      products map {p =>
        Ok(views.html.basketitemadd(basketItemFormCreate, p, baskets))
      }
  }

  def basketItemAddHandle = Action.async { implicit  request =>
      var prod:Seq[Product] = Seq[Product]()
      productRepository.list().onComplete{
        case Success(value) => prod = value
        case Failure(_) => print("fail")
      }

    var bask:Seq[Basket] = Seq[Basket]()
    basketRepository.list().onComplete{
      case Success(value) => bask = value
      case Failure(_) => print("fail")
    }

    basketItemFormCreate.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketitemadd(errorForm, prod, bask))
        )
      },
      basketItem => {
        basketItemRepository.create(1, basketItem.product, basketItem.count, basketItem.basket).map { _ =>
         Ok("Ok")
        }
      }
    )

  }

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
case class UpdateBasketItemForm(var id: Int, var product: Int, var count: Int, var basket: Int)
case class CreateBasketItemForm(var product: Int, var count: Int, var basket: Int)
