package services

import javax.inject.Inject
import models.{User, UserRepository}
import play.api.libs.json.Json
import play.api.mvc.Request
import scalaj.http.Http

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class TokenResponse(var email: String, var userId: String)

class TokenManager @Inject()(userRepository: UserRepository) {

    def verifyToken(token: String) = {
      val result = Http("https://www.googleapis.com/oauth2/v1/tokeninfo").param("access_token", token).asString
      if (result.is2xx){
         true
      }else{
        false
      }
    }

    def getUserInfo(token: String): TokenResponse = {
      val result = Http("https://www.googleapis.com/oauth2/v1/tokeninfo").param("access_token", token)
      val p = Json.parse(result.asString.body);
      TokenResponse.apply(p.result.get("email").toString(), p.result.get("user_id").toString())
    }

    def getUserBy(token: String): User = {
      val loginInfo = getUserInfo(token)
      val user = Await.result(userRepository.getByEmail(loginInfo.email), Duration.Inf)
      user
    }

}
