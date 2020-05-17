import * as React from "react";
import {Component} from "react";
import {Table} from "reactstrap";
import {OrderService} from "../services/OrderService";

export class OrderView extends Component {

    orderService: OrderService;

    constructor() {
        super();
        this.orderService = new OrderService();
        this.state = {orders: []};
    }

    async componentDidMount(): void {
        let fetched = await this.orderService.fetchAll();
        this.setState({orders: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>Basket Id</th>
            <th>Payment Id</th>
        </tr>

        let tableObjects = this.state.orders.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.basketId}</th>
                <th>{p.paymentId}</th>
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