import API_ENDPOINT from "../configuration/Constants";

export class Basket{
    id;
    userId;
}

export class BasketService{

    async fetchAll(): Promise<Array<Basket>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/basket');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }
}