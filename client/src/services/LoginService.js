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
        return loginResult;
    }

    async register(email, password) {
        let result;
        try {
            let options = {
                'method': 'POST',
                'headers': new HeadersFactory().create(),
                body: JSON.stringify({ 'email': email, 'password': password, 'id': 1})
            };

            result = await fetch(API_ENDPOINT + '/register', options);

        }catch (e) {
            console.log(e);
        }
        const parsed = JSON.parse(await result.text());
        return parsed;
    }

    async loginWithEmailAndPassword(email, password): Promise<LoginResponse> {
        let result;
        try {
            let options = {
                'method': 'POST',
                'headers': new HeadersFactory().create(),
                body: JSON.stringify({ 'email': email, 'password': password, 'id': 1})
            };

            result = await fetch(API_ENDPOINT + '/login', options);

        }catch (e) {
            console.log(e);
        }
        const parsed = JSON.parse(await result.text());
        return parsed;
    }

}