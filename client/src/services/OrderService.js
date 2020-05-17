import API_ENDPOINT from "../configuration/Constants";

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
}