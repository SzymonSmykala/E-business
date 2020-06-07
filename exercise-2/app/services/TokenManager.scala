package services

import play.api.libs.json.Json
import scalaj.http.Http

case class TokenResponse(var email: String, var userId: String)

class TokenManager {

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

}
