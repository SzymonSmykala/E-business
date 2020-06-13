import API_ENDPOINT from "../configuration/Constants";
import {HeadersFactory} from "../utils/HeadersFactory";

export class Order{
    id;
    basketId;
    paymentId;
}

export class OrderService {

    async fetchAll(): Promise<Array<Order>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/orders');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }

    async create(basketId, paymentId): Promise<Order> {
        let result;
        try {
            let options = {
                'method': 'POST',
                'headers': new HeadersFactory().create(),
                body: JSON.stringify({"id":1 ,"basketId": basketId, "paymentId": paymentId})
            };

            result = await fetch(API_ENDPOINT + '/orders', options);

        }catch (e) {
            console.log(e);
        }

        return JSON.parse(await result.text());
    }

    async getOrder(orderId): Promise<Order> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/orders/' +  orderId);
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }

    async getOrderByBasketId(basketId):Promise<Array<Order>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/orderByBasket/' +  basketId);
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());

    }
}

