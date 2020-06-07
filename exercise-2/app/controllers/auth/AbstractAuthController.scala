package controllers.auth

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.services.AuthenticatorResult
import models.User
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * `AbstractAuthController` base with support methods to authenticate an user.
  *
  * @param silhouette    The Silhouette stack.
  * @param ex            The execution context.
  */
abstract class AbstractAuthController(silhouette: Silhouette[DefaultEnv])(implicit ex: ExecutionContext) extends InjectedController with I18nSupport {

  /**
    * Performs user authentication
    *
    * @param user       User data
    * @param rememberMe Remember me flag
    * @param request    Initial request
    * @return The result to display.
    */
  protected def authenticateUser(user: UserDTO, loginInfo: LoginInfo, rememberMe: Boolean)(implicit request: Request[_]): Future[AuthenticatorResult] = {
    silhouette.env.authenticatorService.create(loginInfo).map {
      case authenticator => authenticator
    }.flatMap { authenticator =>
      silhouette.env.eventBus.publish(LoginEvent(user, request))
      silhouette.env.authenticatorService.init(authenticator).flatMap { token =>
        silhouette.env.authenticatorService.embed(token, Ok(Json.obj(
          "id" -> user.userID,
          "token" -> token.toString,
          "firstName" -> user.firstName,
          "lastName" -> user.lastName,
          "email" -> user.email
        )))
      }
    }
  }
}