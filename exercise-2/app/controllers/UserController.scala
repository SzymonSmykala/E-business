package controllers

import javax.inject.{Inject, Singleton}
import models.{User, UserRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(cc: ControllerComponents, userRepository: UserRepository) (implicit ec: ExecutionContext) extends AbstractController(cc) {

    def login = Action.async { request =>
      val json = request.body.asJson.get
      val user = json.as[User]
      val result = userRepository.getByEmail(user.email)

      result map {
        r => {
            Ok(Json.toJson(r))
        }
      }
    }

    def register = Action.async { request =>
      var json = request.body.asJson.get
      var user = json.as[User]
      var result = userRepository.create(user.id, user.email, user.password)
      result map {
        r => Ok(Json.toJson(user.id))
      }
    }

}
