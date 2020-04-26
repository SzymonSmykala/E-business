package controllers

import javax.inject.{Inject, _}
import models.{Category, Product, ProductRepository, Promo, PromoRepository}
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


@Singleton
class PromoController @Inject()(cc: MessagesControllerComponents, promoRepository: PromoRepository, productRepository: ProductRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val promoUpdateForm: Form[UpdatePromoForm] = Form {
    mapping(
      "id" -> number,
      "product" -> number,
      "promoAmount" ->number,
    )(UpdatePromoForm.apply)(UpdatePromoForm.unapply)
  }

  val promoCreateForm: Form[CreatePromoForm] = Form {
    mapping(
      "product" -> number,
      "promoAmount" ->number,
    )(CreatePromoForm.apply)(CreatePromoForm.unapply)
  }

  def addPromoForm: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productRepository.list()
    products map {p =>
      Ok(views.html.promoadd(promoCreateForm, p))
    }
  }

  def updatePromoForm(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var products:Seq[Product] = Seq[Product]()
    productRepository.list().onComplete{
      case Success(cat) => products = cat
      case Failure(_) => print("fail")
    }

    val promo = promoRepository.getById(id)
    promo.map(p => {
      val prodForm = promoUpdateForm.fill(UpdatePromoForm(p.id.toInt, p.productId.toInt, p.looseAmount))
      Ok(views.html.promoupdate(prodForm, products))
    })

  }

  def updatePromoHandle = Action.async { implicit  request =>
    var prod:Seq[Product] = Seq[Product]()
    productRepository.list().onComplete{
      case Success(value) => prod = value
      case Failure(_) => print("fail")
    }
    promoUpdateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest("Bad request")
        )
      },
      promo => {
        promoRepository.update(promo.id, Promo(promo.id, promo.product, promo.looseAmount)).map { _ =>
          Redirect(routes.PromoController.updatePromoForm(promo.id)).flashing("success" -> "promo updated")
        }
      }
    )
  }


  def addPromoHandle = Action.async { implicit  request =>
    var prod:Seq[Product] = Seq[Product]()
    productRepository.list().onComplete{
      case Success(value) => prod = value
      case Failure(_) => print("fail")
    }
    promoCreateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.promoadd(errorForm, prod))
        )
      },
      promo => {
        promoRepository.create(1, promo.product, promo.looseAmount).map { _ =>
          Ok("Ok")
        }
      }
    )
  }

  def getPromos: Action[AnyContent] = Action.async { implicit request =>
    val items = promoRepository.list()
    items.map( i => Ok(views.html.promos(i)))
  }

  def get(id: Long) = Action.async {
    val result = promoRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def deletePromo(id: Long): Action[AnyContent] = Action { implicit request =>
    promoRepository.delete(id)
    Redirect(routes.PromoController.getPromos())
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val payment = json.as[Promo]
    val updateResult = promoRepository.update(payment.id, payment)
    updateResult map {r => Ok(Json.toJson(payment))}
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val promo = json.as[Promo]
    val inserted : Future[Promo] = promoRepository.create(promo.id, promo.productId, promo.looseAmount)
    inserted map {
      i =>
        Ok(Json.toJson(i))
    }
  }

  def readAll = Action.async { request =>
    val result = promoRepository.list()
    result.map(prod => Ok(Json.toJson(prod)))
  }

  def delete(id: Long) = Action.async{
    val deleteResult = promoRepository.delete(id)
    deleteResult map {
      r => Ok(Json.toJson(r))
    }
  }
}
case class UpdatePromoForm(var id: Int, var product: Int, var looseAmount: Int)
case class CreatePromoForm(var product: Int, var looseAmount: Int)