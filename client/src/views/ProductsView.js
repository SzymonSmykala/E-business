import * as React from "react";
import {Component} from "react";
import {ProductService} from "../services/ProductService";
import {Table} from "reactstrap";


export class ProductsView extends Component {

    productService: ProductService;

    constructor() {
        super();
        this.productService = new ProductService();
        this.state = {products: []};
    }

    async componentDidMount(): void {
        let fetched = await this.productService.fetchAll();
        this.setState({products: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>Name</th>
            <th>CategoryId</th>
        </tr>

        let tableObjects = this.state.products.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.name}</th>
                <th>{p.categoryId}</th>
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