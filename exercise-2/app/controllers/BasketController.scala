package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.{Basket, BasketRepository, User, UserRepository}
import play.api.data.Forms.mapping
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class BasketController @Inject()(cc: MessagesControllerComponents, basketRepository: BasketRepository, userRepository: UserRepository)(implicit ec: ExecutionContext)  extends MessagesAbstractController(cc) {


  val basketFormUpdate: Form[UpdateBasketForm] = Form {
    mapping(
      "user" -> number
    )(UpdateBasketForm.apply)(UpdateBasketForm.unapply)
  }

  val basketForm: Form[CreateBasketForm] = Form {
    mapping(
      "user" -> number
    )(CreateBasketForm.apply)(CreateBasketForm.unapply)
  }

  def addBasketForm: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = userRepository.list()
    users map {user  => Ok(views.html.basketadd(basketForm, user))}
  }

  def addBasketHandle = Action.async { implicit request =>
    var userSeq:Seq[User] = Seq[User]()
    val categories = userRepository.list().onComplete{
      case Success(cat) => userSeq = cat
      case Failure(_) => print("fail")
    }

    basketForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketadd(errorForm, userSeq))
        )
      },
      product => {
        basketRepository.create(1, product.user.toLong).map { _ =>
          Ok("Ok")
        }
      }
    )

  }

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
case class UpdateBasketForm(var id: Int, var user: Int)
case class CreateBasketForm(var user: Int)