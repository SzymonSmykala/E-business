import API_ENDPOINT from "../configuration/Constants";

export class Payment{
    id;
    status;
}

export class PaymentService{

    async fetchAll(): Promise<Array<Payment>> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/payments');
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }
}