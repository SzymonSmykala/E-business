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

    def getUserInfo(token: String, loginProvider: String): TokenResponse = {
      if (loginProvider == "google") {
        val result = Http("https://www.googleapis.com/oauth2/v1/tokeninfo").param("access_token", token)
        val p = Json.parse(result.asString.body);
        return TokenResponse.apply(p.result.get("email").toString().replaceAll("\"", ""), p.result.get("user_id").toString())
      }

      if (loginProvider == "facebook"){
        val result = Http("https://graph.facebook.com/me").param("fields", "id, email").param("access_token", token);
        val p = Json.parse(result.asString.body);
        return TokenResponse.apply(p.result.get("email").toString().replaceAll("\"", ""), p.result.get("id").toString())
      }
      throw new Exception(loginProvider + " is not a valid Login provider!");
    }

    def getUserBy(token: String, loginProvider: String): User = {
      val loginInfo = getUserInfo(token, loginProvider)
      val user = Await.result(userRepository.getByEmail(loginInfo.email), Duration.Inf)
      user
    }

}
