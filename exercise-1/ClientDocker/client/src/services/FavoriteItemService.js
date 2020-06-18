import API_ENDPOINT from "../configuration/Constants";

export class FavoriteItem{
    id;
    userId;
    productId;
}

export class FavoriteItemService {

    async fetchAll(): Promise<Array<FavoriteItem>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/favoriteItems');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }
}