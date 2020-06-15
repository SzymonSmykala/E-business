package controllers

import javax.inject.{Inject, Singleton}
import models.{User, UserRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{JwtAuthenticator, TokenManager, TokenResponse}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

case class JwtTokenResponse(var jwtToken: String);
object JwtTokenResponse{
  implicit val jwtTokenResponseFormat = Json.format[JwtTokenResponse]
}

@Singleton
class UserController @Inject()(cc: ControllerComponents, userRepository: UserRepository, jwtAuthenticator: JwtAuthenticator, tokenManager: TokenManager) (implicit ec: ExecutionContext) extends AbstractController(cc) {

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

  def addToRepo(info: TokenResponse) = {
    val user = userRepository.create(1, info.email, info.userId);
    user map {u =>
      Ok("Added new")
    }
  }

  def loginUsingAccessTokenToken = Action.async { request =>
    val token = request.headers.get("token")
    val loginProvider = request.headers.get("loginProvider");
    val jwtToken = jwtAuthenticator.generateToken(loginProvider.get, token.get);
    val info = tokenManager.getUserInfo(token.get.toString, loginProvider.get);

    try {
      val result = Await.result(userRepository.getByEmail(info.email), Duration.Inf)
      if (result.email.isBlank || result == null || result.email.isEmpty){
        addToRepo(info);
      }
    } catch {
      case _: Throwable => addToRepo(info)
    }

    Future(Ok(Json.toJson(JwtTokenResponse(jwtToken))));

  }

    def register = Action.async { request =>
      val json = request.body.asJson.get
      val user = json.as[User]
      val result = userRepository.create(user.id, user.email, user.password)
      result map {
        r => Ok(Json.toJson(user.id))
      }
    }

}
