import * as React from "react";
import {Component} from "react";
import {ProductService} from "../services/ProductService";
import {Button, Table} from "reactstrap";
import {CategoryService} from "../services/CategoryService";
import {BasketService} from "../services/BasketService";
import {BasketItemService} from "../services/BasketItemService";


export class ProductsView extends Component {

    productService: ProductService;
    categoryService: CategoryService;
    basketService: BasketService;
    basketItemService: BasketItemService;
    defaultUserId = 1;

    constructor() {
        super();
        this.basketService = new BasketService();
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
        this.basketItemService = new BasketItemService();
        this.state = {products: [], categories: new Map(), basket: "", fetched: false};
    }

    async componentDidMount(): void {
        let fetched = await this.productService.fetchAll();
        this.setState({products: fetched});
        let categories  = await this.categoryService.fetchAll();
        categories.map(c => (this.state.categories.set(c.id, c.name)));
        let basket = await this.basketService.getActiveBasketByUserId(this.defaultUserId);
        this.setState({basket: basket});
        this.setState({fetched:true})

    }

    render() {
        if (this.state.fetched === false) return null;

        const tableHeader =  <tr>
            <th>Id</th>
            <th>Name</th>
            <th>CategoryId</th>
            <th>Actions</th>
        </tr>

        let tableObjects = this.state.products.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.name}</th>
                <th>{this.state.categories.get(p.categoryId)}</th>
                <th><Button onClick={() => this.handleProductAddToBasketClick(p.id)}>Add to basket</Button> </th>
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

    handleProductAddToBasketClick =  async productId => {
        console.log("Adding: " + productId);
        let basket = await this.basketService.getActiveBasketByUserId(this.defaultUserId);
        await this.basketItemService.add(basket.id, productId);
    }
}