import * as React from "react";
import {Component} from "react";
import {Table} from "reactstrap";
import {BasketService} from "../services/BasketService";

export class BasketView extends Component {

    basketService: BasketService;

    constructor() {
        super();
        this.basketService = new BasketService();
        this.state = {baskets: []};
    }

    async componentDidMount(): void {
        let fetched = await this.basketService.fetchAll();
        this.setState({baskets: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>UserId</th>
        </tr>

        let tableObjects = this.state.baskets.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.userId}</th>
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