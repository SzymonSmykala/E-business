package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.ProductRepository
import repositories.DTO.Product

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}


@Singleton
class ProductController @Inject()(productsRepository: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def get(id: Long) = Action.async {
    val result = productsRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def update() = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Product]
    print(product)
    Ok(Json.toJson(product))
  }

  def add():Action[AnyContent] = Action.async{ implicit request: MessagesRequest[AnyContent]  =>
      val json = request.body.asJson.get
      val product = json.as[Product]
      val inserted : Future[Product] = productsRepository.create(product.name, product.categoryId, product.id)
      inserted map {
        i =>
          Ok(Json.toJson(i))
      }
  }

  def readAll: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val products = productsRepository.list()
    products.map(prod => Ok(Json.toJson(prod)))
  }


  def delete(id: Long) = Action{
    Ok(Json.toJson("completed"))
  }
}
