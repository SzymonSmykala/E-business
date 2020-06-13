import {HeadersFactory} from "../utils/HeadersFactory";
import API_ENDPOINT from "../configuration/Constants";
import {Order} from "./OrderService";

export class LoginService{

    async login(): Promise<Order> {
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

        return JSON.parse(await result.text());
    }

}