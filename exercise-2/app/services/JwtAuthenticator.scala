package services

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import com.google.inject.Inject
import models.{User, UserRepository}
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.concurrent.duration.Duration



case class UserLogin(email: String, userId: Long)
object UserLogin{
  implicit val UserLoginFormat = Json.format[UserLogin]
}

class JwtAuthenticator @Inject()(tokenManager: TokenManager, userRepository: UserRepository) {

  def getUserByToken (jwtToken: String): User = {
    if (JwtHelper.isValidToken(jwtToken)){
      val decoded = JwtHelper.decodePayload(jwtToken).get
      val user = Json.parse(decoded)
      val email = user.result.get("email").toString().replaceAll("\"", "");
      val userId = user.result.get("userId").toString().replaceAll("\"", "");

      val userFromDb = Await.result(userRepository.getById(userId.toLong), Duration.Inf);
      if (email == userFromDb.email.replaceAll("\"", "")){
        return userFromDb
      }else{
        throw new Exception("Token is not valid. Token: " + jwtToken + " Decoded user: " + email + " email from db:" + userFromDb.email)
      }

    }else{
      throw new Exception("Provided value doesn't look like jwt token! Token: " + jwtToken)
    }
  }

  def generateToken(email: String): String={
    val user = Await.result(userRepository.getByEmail(email),Duration.Inf)
    val userLogin = UserLogin(user.email, user.id);
    val jwtToken = JwtHelper.createToken(Json.toJson(userLogin).toString());
    jwtToken
  }

  def generateToken(loginProvider: String, accessToken: String): String ={
    val user = tokenManager.getUserBy(accessToken, loginProvider);
    val userLogin = UserLogin(user.email, user.id);
    val jwtToken = JwtHelper.createToken(Json.toJson(userLogin).toString());
    jwtToken
  }

}