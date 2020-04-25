package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.{Payment, PaymentRepository}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Form
import play.api.data.Forms._


import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PaymentController @Inject()(cc: MessagesControllerComponents, paymentRepository: PaymentRepository)(implicit ec: ExecutionContext)  extends MessagesAbstractController(cc) {

  val paymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "status" -> nonEmptyText,
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  def addPaymentForm: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.addpayment(paymentForm))
  }

  def addPaymentHandle = Action.async { implicit request =>

    paymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.addpayment(errorForm))
        )
      },
      payment => {
        paymentRepository.create(1, payment.status).map { _ =>
          Ok("Ok")
        }
      }
    )

  }

  def get(id: Long) = Action.async {
    val result = paymentRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val payment = json.as[Payment]
    val updateResult = paymentRepository.update(payment.id, payment)
    updateResult map {r => Ok(Json.toJson(payment))}
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val payment = json.as[Payment]
    val inserted : Future[Payment] = paymentRepository.create(payment.id, payment.status)
    inserted map {
      i =>
        Ok(Json.toJson(i))
    }
  }

  def readAll = Action.async { request =>
    val result = paymentRepository.list()
    result.map(prod => Ok(Json.toJson(prod)))
  }

  def delete(id: Long) = Action.async{
    val deleteResult = paymentRepository.delete(id)
    deleteResult map {
      r => Ok(Json.toJson(r))
    }
  }

}

case class CreatePaymentForm(var status: String)