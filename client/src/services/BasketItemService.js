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
}