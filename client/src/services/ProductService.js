import API_ENDPOINT from "../configuration/Constants";

export class Product{
    id;
    categoryId;
    name;
}

export class ProductService{

    async fetchAll(): Promise<Array<Product>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/products');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }
}