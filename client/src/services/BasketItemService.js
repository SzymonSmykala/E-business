import API_ENDPOINT from "../configuration/Constants";
import {HeadersFactory} from "../utils/HeadersFactory";

export class BasketItem{
    id;
    productId;
    count;
    basketId;
}

export class BasketItemService{

    async fetchAll(): Promise<Array<BasketItem>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/basketItems');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }

    async add(basketId, productId): Promise<BasketItem> {
        let result;
        try {
            let options = {
                'method': 'POST',
                'headers': new HeadersFactory().create(),
                body: JSON.stringify({"id":0, "productId": productId,"count":1, "basketId": basketId})
            };
            console.log(options);
            result = await fetch(API_ENDPOINT + '/basketItems', options)

        }catch (e) {
            console.log(e);
        }

        return JSON.parse(await result.text());
    }

    async fetchBasketItemsByBasketId(basketId) : Promise<Array<BasketItem>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/basketItems/basket/' + basketId,
                {headers: new HeadersFactory().create()});;
        }catch (e) {
            console.log(e);
        }
        console.log("fetching basket items");
        let parsed = JSON.parse(await result.text());
        console.log(parsed);
        return parsed;
    }
}