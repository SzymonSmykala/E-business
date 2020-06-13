package controllers

import java.util.concurrent.TimeUnit

import javax.inject.{Inject, _}
import models._
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.libs.json._
import play.api.mvc._
import services.TokenManager

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class BasketItemsController @Inject()(cc: MessagesControllerComponents, basketItemRepository: BasketItemRepository, productRepository: ProductRepository, basketRepository: BasketRepository, userRepository: UserRepository, tokenManager: TokenManager)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val basketItemFormUpdate: Form[UpdateBasketItemForm] = Form {
    mapping(
      "id" -> number,
      "product" -> number,
      "count" ->number,
      "basket" -> number,
    )(UpdateBasketItemForm.apply)(UpdateBasketItemForm.unapply)
  }

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

  def updateBasketItem(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var baskets:Seq[Basket] = Seq[Basket]()
    basketRepository.list().onComplete{
      case Success(cat) => baskets = cat
      case Failure(_) => print("fail")
    }

    var products: Seq[Product] = Seq[Product]()
    productRepository.list().onComplete{
      case Success(p) => products = p
      case Failure(_) => print("fail")
    }

    val basket = basketItemRepository.getById(id)
    basket.map(b => {
      val prodForm = basketItemFormUpdate.fill(UpdateBasketItemForm(id.toInt, b.productId.toInt, b.count, b.basketId.toInt))
      Ok(views.html.basketitemupdate(prodForm, products, baskets))
    })
  }

  def updateBasketItemHandle(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var baskets:Seq[Basket] = Seq[Basket]()
    basketRepository.list().onComplete{
      case Success(cat) => baskets = cat
      case Failure(_) => print("fail")
    }

    var products: Seq[Product] = Seq[Product]()
    productRepository.list().onComplete{
      case Success(p) => products = p
      case Failure(_) => print("fail")
    }

    basketItemFormUpdate.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketitemupdate(errorForm, products, baskets))
        )
      },
      product => {
        basketItemRepository.update(product.id, BasketItem(product.id, product.product, product.count, product.basket)).map { _ =>
          Redirect(routes.BasketItemsController.updateBasketItem(product.id)).flashing("success" -> "basket item updated")
        }
      }
    )

  }

  def getBasketItems: Action[AnyContent] = Action.async { implicit request =>
    val items = basketItemRepository.list()
    items.map( p => Ok(views.html.basketitems(p)))
  }

  def deleteBasketItem(id: Long): Action[AnyContent] = Action { implicit request =>
    basketItemRepository.delete(id)
    Redirect(routes.BasketItemsController.getBasketItems())
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
    val token = request.headers.get("token")
    val loginProvider = request.headers.get("loginProvider")
    val user = tokenManager.getUserBy(token.get, loginProvider.get)

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

  def getByBasketId(basketId: Long) = Action.async{
    val result = basketItemRepository.getBasketItemsByBasketId(basketId)
    result map {
      r => Ok(Json.toJson(r))
    }
  }
}
case class UpdateBasketItemForm(var id: Int, var product: Int, var count: Int, var basket: Int)
case class CreateBasketItemForm(var product: Int, var count: Int, var basket: Int)
