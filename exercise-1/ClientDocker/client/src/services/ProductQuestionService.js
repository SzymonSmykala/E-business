import API_ENDPOINT from "../configuration/Constants";

export class ProductQuestion{
    id;
    productId;
    questionContent;
}

export class ProductQuestionService{

    async fetchAll(): Promise<Array<ProductQuestion>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/productQuestions');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }
}