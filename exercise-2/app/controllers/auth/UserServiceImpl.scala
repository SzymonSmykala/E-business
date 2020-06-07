package controllers.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import controllers.UserService
import javax.inject.Inject
import models.User

import scala.concurrent.{ExecutionContext, Future}


class UserServiceImpl @Inject()()(implicit ec: ExecutionContext) extends UserService {


  def retrieve(loginInfo: LoginInfo): Future[Option[UserDTO]] = {
    Future(Option(UserDTO(UUID.randomUUID(), Option("S"), Option("s"), Option(""), Option(""))))
  }
}
