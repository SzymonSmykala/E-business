package controllers

import java.util.Date
import java.util.concurrent.TimeUnit

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.{Promo, PromoRepository}
import org.joda.time.DateTime
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration


@Singleton
class PromoController @Inject()(cc: MessagesControllerComponents, promoRepository: PromoRepository)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val promoFormCreate: Form[CreatePromoForm] = Form {
    mapping(
      "product" -> number,
      "promoAmount" ->number,
    )(CreatePromoForm.apply)(CreatePromoForm.unapply)
  }

//  def addPromoForm: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
//    val payments = paymentRepository.list()
//    val baskets = Await.result(basketRepository.list(), Duration(10, TimeUnit.SECONDS));
//    payments map {p =>
//      Ok(views.html.orderadd(orderFormCreate, baskets, p))
//    }
//  }

  def get(id: Long) = Action.async {
    val result = promoRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
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

case class CreatePromoForm(var product: Int, var looseAmount: Int)