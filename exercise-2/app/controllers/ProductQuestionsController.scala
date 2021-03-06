package controllers

import javax.inject.{Inject, _}
import models.{Product, ProductQuestion, ProductQuestionRepository, ProductRepository}
import play.api.data.Form
import play.api.data.Forms.{mapping, _}
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ProductQuestionsController @Inject()(cc: MessagesControllerComponents, productQuestionRepository: ProductQuestionRepository, productRepository: ProductRepository) (implicit ec: ExecutionContext)extends MessagesAbstractController(cc) {


  val productQuestionUpdateForm: Form[ProductQuestionUpdateForm] = Form {
    mapping(
            "id"  -> number,
      "product" -> number,
      "question" -> nonEmptyText,
    )(ProductQuestionUpdateForm.apply)(ProductQuestionUpdateForm.unapply)
  }

  val productQuestionCreateForm: Form[ProductQuestionCreateForm] = Form {
    mapping(
      "product" -> number,
      "question" -> nonEmptyText,
    )(ProductQuestionCreateForm.apply)(ProductQuestionCreateForm.unapply)
  }

  def addProductQuestionForm(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productRepository.list()
    products map {p =>
      Ok(views.html.productquestionadd(productQuestionCreateForm, p))
    }
  }

  def addProductQuestionHandle() = Action.async { implicit request =>
    var prod:Seq[Product] = Seq[Product]()
    productRepository.list().onComplete{
      case Success(value) => prod = value
      case Failure(_) => print("fail")
    }
    productQuestionCreateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.productquestionadd(errorForm, prod))
        )
      },
      promo => {
        productQuestionRepository.create(1, promo.product, promo.questionContent).map { _ =>
          Ok("Ok")
        }
      }
    )
  }

  def getProductQuestions: Action[AnyContent] = Action.async { implicit request =>
    val items = productQuestionRepository.list()
    items.map( p => Ok(views.html.productquestions(p)))
  }

  def deleteProductQuestion(id: Long): Action[AnyContent] = Action { implicit request =>
    productQuestionRepository.delete(id)
    Redirect(routes.ProductQuestionsController.getProductQuestions())
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

case class ProductQuestionUpdateForm(var id: Int, var product: Int, var questionContent: String)
case class ProductQuestionCreateForm(var product: Int, var questionContent: String)