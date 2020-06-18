import * as React from "react";
import {Component} from "react";
import {Table} from "reactstrap";
import {PromoService} from "../services/PromoService";


export class PromosView extends Component {

    promosService: PromoService;

    constructor() {
        super();
        this.promosService = new PromoService();
        this.state = {promos: []};
    }

    async componentDidMount(): void {
        let fetched = await this.promosService.fetchAll();
        this.setState({promos: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>ProductId</th>
            <th>Loose Amount</th>
        </tr>

        let tableObjects = this.state.promos.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.productId}</th>
                <th>{p.looseAmount}</th>
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