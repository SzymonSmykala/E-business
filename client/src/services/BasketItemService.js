import API_ENDPOINT from "../configuration/Constants";

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
                'headers': {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({"id":0, "productId": productId,"count":1, "basketId": basketId})

            };

            result = await fetch(API_ENDPOINT + '/basketItems', options);

        }catch (e) {
            console.log(e);
        }

        return JSON.parse(await result.text());
    }

    async fetchBasketItemsByBasketId(basketId) : Promise<Array<BasketItem>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/basketItems/basket/' + basketId);
        }catch (e) {
            console.log(e);
        }
        console.log("fetching basket items");
        let parsed = JSON.parse(await result.text());
        console.log(parsed);
        return parsed;

    }
}