import * as React from "react";
import {Component} from "react";
import {Button, Table} from "reactstrap";
import {PaymentService} from "../services/PaymentService";


export class PaymentsView extends Component {

    paymentService: PaymentService;

    constructor() {
        super();
        this.paymentService = new PaymentService();
        this.state = {payments: []};
    }

    async componentDidMount(): void {
        let fetched = await this.paymentService.fetchAll();
        this.setState({payments: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>Status</th>
        </tr>

        let tableObjects = this.state.payments.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.status}</th>
            </tr>

        ));
        return <div>
            <div style={{display: 'flex',  justifyContent:'center', alignItems:'center', height: '100vh', marginLeft: '10vh', marginRight: '10vh'}}>
                <Table>
                    <thead>
                    {tableHeader}
                    </thead>
                    <tbody>
                    {tableObjects}
                    </tbody>
                </Table>
            </div>

        </div>
    }

}