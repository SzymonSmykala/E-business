import * as React from "react";
import {Component} from "react";
import {Table} from "reactstrap";
import {BasketItemService} from "../services/BasketItemService";

export class BasketItemsView extends Component {

    basketItemService: BasketItemService;

    constructor() {
        super();
        this.basketItemService = new BasketItemService();
        this.state = {items: []};
    }

    async componentDidMount(): void {
        let fetched = await this.basketItemService.fetchAll();
        this.setState({items: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>Product Id</th>
            <th>Count</th>
            <th>Basket Id</th>
        </tr>

        let tableObjects = this.state.items.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.productId}</th>
                <th>{p.count}</th>
                <th>{p.basketId}</th>
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