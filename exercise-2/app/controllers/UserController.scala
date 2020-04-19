package controllers

import javax.inject.{Inject, Singleton}
import models.User
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

    def login = Action { request =>
      var json = request.body.asJson.get
      var user = json.as[User]
      Ok(Json.toJson("Success"))
    }

    def register = Action { request =>
      var json = request.body.asJson.get
      var user = json.as[User]
      Ok(Json.toJson("Success"))
    }

}
