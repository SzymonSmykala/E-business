package controllers

import javax.inject.{Inject, _}
import models.{ProductQuestion, ProductQuestionRepository}
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class ProductQuestionsController @Inject()(cc: ControllerComponents, productQuestionRepository: ProductQuestionRepository) (implicit ec: ExecutionContext)extends AbstractController(cc) {

  val productQuestionCreateForm: Form[ProductQuestionCreateForm] = Form {
    mapping(
      "product" -> number,
      "question" -> nonEmptyText,
    )(ProductQuestionCreateForm.apply)(ProductQuestionCreateForm.unapply)
  }

  def readAll: Action[AnyContent] = Action.async {
    val result = productQuestionRepository.list()
    result map { r => Ok(Json.toJson(r));};
  }

  def get(id: Long) = Action.async {
    val result = productQuestionRepository.getById(id)
    result map { r => Ok(Json.toJson(r));};
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val product = json.as[ProductQuestion]
    val result = productQuestionRepository.update(product.id, product)
    result map {
      r => Ok(Json.toJson(product))
    }
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val productQuestion = json.as[ProductQuestion]
    val result = productQuestionRepository.create(productQuestion.id, productQuestion.productId, productQuestion.questionContent)
    result map {
      r => Ok(Json.toJson(productQuestion))
    }
  }

  def delete(id: Long) = Action.async{
    val result = productQuestionRepository.delete(id)
    result map {
      r => Ok(Json.toJson(r))
    }
  }
}


case class ProductQuestionCreateForm(var product: Int, var questionContent: String)