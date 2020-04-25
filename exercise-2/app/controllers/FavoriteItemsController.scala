package controllers

import javax.inject.{Inject, _}
import models.{FavoriteItem, FavoriteItemsRepository, ProductRepository, UserRepository}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.libs.json._
import play.api.data.Forms._
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.ExecutionContext


@Singleton
class FavoriteItemsController @Inject()(cc: MessagesControllerComponents, favoriteItemsRepository: FavoriteItemsRepository, userRepository: UserRepository, productRepository: ProductRepository) (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[FavoriteItemCreateForm] = Form {
    mapping(
      "user" -> number,
      "product" -> number,
    )(FavoriteItemCreateForm.apply)(FavoriteItemCreateForm.unapply)
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