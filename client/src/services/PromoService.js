import API_ENDPOINT from "../configuration/Constants";

export class Promo{
    id;
    productId;
    looseAmount;
}

export class PromoService{

    async fetchAll(): Promise<Array<Promo>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/promos');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }
}