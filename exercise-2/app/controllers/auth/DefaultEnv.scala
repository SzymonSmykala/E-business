package controllers.auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.User

/**
 * The default env.
 */
trait DefaultEnv extends Env {
  type I = UserDTO
  type A = JWTAuthenticator
}