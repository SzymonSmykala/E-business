import API_ENDPOINT from "../configuration/Constants";

export class Category{
    id;
    name;
}

export class CategoryService{

    async fetchAll(): Promise<Array<Category>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/category');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }
}