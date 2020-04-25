package controllers

import java.util.concurrent.TimeUnit

import javax.inject.{Inject, _}
import models.{FavoriteItem, FavoriteItemsRepository, Product, ProductRepository, User, UserRepository}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.libs.json._
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class FavoriteItemsController @Inject()(cc: MessagesControllerComponents, favoriteItemsRepository: FavoriteItemsRepository, userRepository: UserRepository, productRepository: ProductRepository) (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val favoriteItemForm: Form[FavoriteItemCreateForm] = Form {
    mapping(
      "user" -> number,
      "product" -> number,
    )(FavoriteItemCreateForm.apply)(FavoriteItemCreateForm.unapply)
  }

  def addFavoriteItemForm: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productRepository.list()
    val users = Await.result(userRepository.list(), Duration(10, TimeUnit.SECONDS));
    products map {p =>
      Ok(views.html.favoriteitemadd(favoriteItemForm, p, users))
    }
  }

  def addFavoriteItemHandle = Action.async { implicit  request =>
    var prod:Seq[Product] = Seq[Product]()
    productRepository.list().onComplete{
      case Success(value) => prod = value
      case Failure(_) => print("fail")
    }

    var user:Seq[User] = Seq[User]()
    userRepository.list().onComplete{
      case Success(value) => user = value
      case Failure(_) => print("fail")
    }

    favoriteItemForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.favoriteitemadd(errorForm, prod, user))
        )
      },
      favItem => {
        favoriteItemsRepository.create(1, favItem.user, favItem.product).map { _ =>
          Ok("Ok")
        }
      }
    )
  }

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

case class FavoriteItemCreateForm(var user: Int, var product: Int)