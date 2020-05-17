import * as React from "react";
import {Component} from "react";
import {ProductService} from "../services/ProductService";
import {Table} from "reactstrap";
import {CategoryService} from "../services/CategoryService";


export class ProductsView extends Component {

    productService: ProductService;
    categoryService: CategoryService;

    constructor() {
        super();
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
        this.state = {products: [], categories: new Map(), fetched: false};
    }

    async componentDidMount(): void {
        let fetched = await this.productService.fetchAll();
        this.setState({products: fetched});
        let categories  = await this.categoryService.fetchAll();
        categories.map(c => (this.state.categories.set(c.id, c.name)));
        this.setState({fetched:true})

    }

    render() {
        if (this.state.fetched === false) return null;

        const tableHeader =  <tr>
            <th>Id</th>
            <th>Name</th>
            <th>CategoryId</th>
        </tr>

        let tableObjects = this.state.products.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.name}</th>
                <th>{this.state.categories.get(p.categoryId)}</th>
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