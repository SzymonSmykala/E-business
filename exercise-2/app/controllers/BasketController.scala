package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.{Basket, BasketRepository, Category, OrderRepository, PaymentRepository, Product, User, UserRepository}
import play.api.data.Forms.mapping
import play.api.data.Form
import play.api.data.Forms._
import services.{JwtAuthenticator, TokenManager}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class BasketController @Inject()(cc: MessagesControllerComponents, basketRepository: BasketRepository, userRepository: UserRepository, tokenManager: TokenManager, paymentRepository: PaymentRepository, orderRepository: OrderRepository, jwtAuthenticator: JwtAuthenticator)(implicit ec: ExecutionContext)  extends MessagesAbstractController(cc) {


  val basketFormUpdate: Form[UpdateBasketForm] = Form {
    mapping(
      "id" -> number,
      "user" -> number
    )(UpdateBasketForm.apply)(UpdateBasketForm.unapply)
  }

  val basketForm: Form[CreateBasketForm] = Form {
    mapping(
      "user" -> number
    )(CreateBasketForm.apply)(CreateBasketForm.unapply)
  }

  def addBasketForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = userRepository.list()
    users map {user  => Ok(views.html.basketadd(basketForm, user))}
  }

  def addBasketHandle(): Action[AnyContent] = Action.async { implicit request =>
    var userSeq:Seq[User] = Seq[User]()
    userRepository.list().onComplete{
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

  def getBaskets: Action[AnyContent] = Action.async { implicit request =>
    val produkty = basketRepository.list()
    produkty.map( products => Ok(views.html.baskets(products)))
  }

  def deleteBasket(id: Long): Action[AnyContent] = Action { implicit request =>
    basketRepository.delete(id)
    Redirect(routes.BasketController.getBaskets())
  }

  def updateBasket(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var users:Seq[User] = Seq[User]()
    userRepository.list().onComplete{
      case Success(cat) => users = cat
      case Failure(_) => print("fail")
    }
    val produkt = basketRepository.getById(id)
    produkt.map(product => {
      val prodForm = basketFormUpdate.fill(UpdateBasketForm(product.id.toInt, product.userId.toInt))
      Ok(views.html.basketupdate(prodForm, users))
    })
  }

  def updateBasketHandle(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var categ:Seq[User] = Seq[User]()
    userRepository.list().onComplete{
      case Success(cat) => categ = cat
      case Failure(_) => print("fail")
    }

    basketFormUpdate.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketupdate(errorForm, categ))
        )
      },
      product => {
        basketRepository.update(product.id, Basket(product.id, product.user)).map { _ =>
          Redirect(routes.BasketController.updateBasket(product.id)).flashing("success" -> "product updated")
        }
      }
    )

  }

  def readAll: Action[AnyContent] = Action.async {
    val result = basketRepository.list()
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def get(id: Long): Action[AnyContent] = Action.async {
    val result = basketRepository.getById(id);
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def add(): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val product = json.as[Basket]
    val result = basketRepository.create(product.id, product.userId)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def delete(id: Long): Action[AnyContent] = Action.async {
    val result = basketRepository.delete(id)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def addtoRepo(id: Long) = {
    val basket = Await.result(basketRepository.create(1, id), Duration.Inf)
    Ok(Json.toJson(basket))
  }

  def getByUserId(userId: Long) = Action { implicit request =>
    val user = tokenManager.getUserBy(request.headers.get("token").get, request.headers.get("loginProvider").get)
    val user12 = jwtAuthenticator.getUserByToken(request.headers.get("jwtToken").get);
    var result: Basket = null;
    try {
      val baskets = Await.result(basketRepository.getAllBasketsByUser(user), Duration.Inf);
      for (basket <- baskets){
        var orders = Await.result(orderRepository.getByBasketId(basket.id), Duration.Inf);
        for (order <- orders){
          val payment = Await.result(paymentRepository.getById(order.paymentId), Duration.Inf);
          if (payment.status != "completed"){
            result = basket;
//            Ok(Json.toJson(basket))
          }
        }
      }
      if (result == null) {
        val basket = Await.result(basketRepository.create(1, user.id), Duration.Inf)
        val payment = Await.result(paymentRepository.create(1, "new"), Duration.Inf);
        Await.result(orderRepository.create(1, basket.id, payment.id), Duration.Inf);
        result = basket
      }
      Ok(Json.toJson(result))

    }catch{
      case _: Throwable => addtoRepo(user.id)
    }
  }

}
case class UpdateBasketForm(var id: Int, var user: Int)
case class CreateBasketForm(var user: Int)