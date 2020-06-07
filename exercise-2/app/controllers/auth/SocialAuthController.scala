package controllers.auth

import java.util.UUID

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.impl.providers._
import javax.inject.Inject
import play.api.mvc.{AnyContent, ControllerComponents, Request}

import scala.concurrent.{ExecutionContext, Future}

/**
  * The social auth controller.
  *
  * @param components             The Play controller components.
  * @param silhouette             The Silhouette stack.
  * @param authInfoRepository     The auth info service implementation.
  * @param socialProviderRegistry The social provider registry.
  * @param ex                     The execution context.
  */
class SocialAuthController @Inject()(components: ControllerComponents,
                                     silhouette: Silhouette[DefaultEnv],
                                     authInfoRepository: AuthInfoRepository,
                                     socialProviderRegistry: SocialProviderRegistry)
                                    (implicit ex: ExecutionContext) extends AbstractAuthController(silhouette) with Logger {

  /**
    * Authenticates a user against a social provider.
    *
    * @param provider The ID of the provider to authenticate against.
    * @return The result to display.
    */
  def authenticate(provider: String) = Action.async { implicit request: Request[AnyContent] =>
    (socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            userBindResult <- provideUserForSocialAccount(provider, profile, authInfo)
            result <- userBindResult match {
              case AccountBound(u) =>
                authenticateUser(u, profile.loginInfo, rememberMe = true)
//              case EmailIsBeingUsed(providers) =>
//                Future.successful(Conflict(Json.obj("error" -> "EmailIsBeingUsed", "providers" -> providers)))
//              case NoEmailProvided =>
//                Future.successful(BadRequest(Json.obj("error" -> "NoEmailProvided")))
            }
          } yield result
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recover {
      case e: ProviderException =>
        logger.error("Unexpected provider error", e)
        Redirect("/error?message=socialAuthFailed")
    }
  }

  def provideUserForSocialAccount[T <: AuthInfo](provider: String, profile: CommonSocialProfile, authInfo: T): Future[UserForSocialAccountResult] = {
    profile.email match {
      case Some(email) =>
        for {
          user <- Future(UserDTO(UUID.randomUUID(),
            profile.email,
            profile.firstName,
            profile.lastName, profile.avatarURL))

          //              _ <- addAuthenticateMethod(user.userID, profile.loginInfo, authInfo)
          _ <- authInfoRepository.add(profile.loginInfo, authInfo)
        } yield AccountBound(user)

      case None => throw new Exception()
    }
  }
}

sealed trait UserForSocialAccountResult

case class AccountBound(user: UserDTO) extends UserForSocialAccountResult
