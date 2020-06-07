package controllers

import com.mohiva.play.silhouette.api.services.IdentityService
import controllers.auth.UserDTO

/**
  * Handles actions to users.
  */
trait UserService extends IdentityService[UserDTO] {

}
