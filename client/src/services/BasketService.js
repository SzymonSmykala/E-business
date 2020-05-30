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

    async create(userId): Promise<Basket> {
        let result;
        try {
            let options = {
                'method': 'POST',
                'headers': {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({"id":1 ,"userId":userId})

            };

            result = await fetch(API_ENDPOINT + '/basket', options);

        }catch (e) {
            console.log(e);
        }

        return JSON.parse(await result.text());
    }

    async getActiveBasketByUserId(userId) : Promise<Basket>{
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/basket/user/' + userId);
        }catch (e) {
            console.log(e);
            throw new Error();
        }
        return JSON.parse(await result.text());
    }
}