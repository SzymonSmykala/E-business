package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesRequest}
import play.api.libs.json._
import javax.inject._
import models.{Category, CategoryRepository}

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CategoryController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext, categoryRepository: CategoryRepository)  extends AbstractController(cc) {

  def get(id: Long) = Action.async {
    val result = categoryRepository.getById(id)
    result map {r =>
      Ok(Json.toJson(r));
    }
  }

  def update() = Action.async { request =>
    val json = request.body.asJson.get
    val category = json.as[Category]
    val updateResult = categoryRepository.update(category.id, category)
    updateResult map {r => Ok(Json.toJson(category))}
  }

  def add() = Action.async { request =>
    val json = request.body.asJson.get
    val category = json.as[Category]
    val inserted : Future[Category] = categoryRepository.create(category.id, category.name)
    inserted map {
      i =>
        Ok(Json.toJson(i))
    }
  }

  def readAll = Action.async { request =>
    val result = categoryRepository.list()
    result.map(prod => Ok(Json.toJson(prod)))
  }

  def delete(id: Long) = Action.async{
    val deleteResult = categoryRepository.delete(id)
    deleteResult map {
      r => Ok(Json.toJson(r))
    }
  }

}
