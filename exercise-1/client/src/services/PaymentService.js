import API_ENDPOINT from "../configuration/Constants";
import {HeadersFactory} from "../utils/HeadersFactory";

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

    async createNewPayment(): Promise<Payment> {
        let result;
        try {
            let options = {
                'method': 'POST',
                'headers': new HeadersFactory().create(),
                body: JSON.stringify({"id":1 ,"status": "new"})
            };

            result = await fetch(API_ENDPOINT + '/payments', options);

        }catch (e) {
            console.log(e);
        }

        return JSON.parse(await result.text());
    }

    async getPayment(paymentId): Promise<Payment> {
        let result;
        try {
            result = await fetch(API_ENDPOINT + '/payments/' + paymentId);
        }catch (e) {
            console.log(e);
        }
        return JSON.parse(await result.text());
    }

    async completePayment(paymentId): Promise<Payment> {
        let result;
        console.log("Complete payment For:" + paymentId)
        try {
            let options = {
                'method': 'PUT',
                'headers': new HeadersFactory().create(),
                body: JSON.stringify({"id":paymentId ,"status": "completed"})
            };

            result = await fetch(API_ENDPOINT + '/payments', options);

        }catch (e) {
            console.log(e);
        }

        return JSON.parse(await result.text());
    }

}