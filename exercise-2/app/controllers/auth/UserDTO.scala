package controllers.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.Identity

/**
  * The user object.
  *
  * @param userID    The unique ID of the user.
  * @param firstName Maybe the first name of the authenticated user.
  * @param lastName  Maybe the last name of the authenticated user.
  * @param email     Maybe the email of the authenticated provider.
  * @param avatarURL Maybe the avatar URL of the authenticated provider.
  */
case class UserDTO(userID: UUID,
                firstName: Option[String],
                lastName: Option[String],
                email: Option[String],
                avatarURL: Option[String],
               ) extends Identity
