import {HeadersFactory} from "../utils/HeadersFactory";
import API_ENDPOINT from "../configuration/Constants";

class LoginResponse {
    jwtToken;
}

export class LoginService{

    async login() : Promise<LoginResponse> {
        let result;
        try {
            let options = {
                'method': 'GET',
                'headers': new HeadersFactory().create()
            };

            result = await fetch(API_ENDPOINT + '/tokenLogin', options);

        }catch (e) {
            console.log(e);
        }
        const loginResult = JSON.parse(await result.text());
        console.log("LOGIN RESULT:");
        console.log(loginResult);
        return loginResult;
    }

}