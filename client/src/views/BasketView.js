import * as React from "react";
import {Component} from "react";
import {Table} from "reactstrap";
import {BasketService} from "../services/BasketService";
import {Product, ProductService} from "../services/ProductService";
import {BasketItemService} from "../services/BasketItemService";

export class BasketView extends Component {

    basketService: BasketService;
    productService: ProductService;
    basketItemsService: BasketItemService;
    defaultUserId = 1;

    constructor() {
        super();
        this.basketService = new BasketService();
        this.basketItemsService = new BasketItemService();
        this.productService = new ProductService();
        this.state = {baskets: [], products: new Map(), basketItems: [], fetched: false};
    }

    async componentDidMount(): void {

        let fetched = await this.basketService.fetchAll();
        let products = await this.productService.fetchAll();
        products.map(p => this.state.products.set(p.id, p));
        let basket = await this.basketService.getActiveBasketByUserId(this.defaultUserId);
        let basketItems = await this.basketItemsService.fetchBasketItemsByBasketId(basket.id);
        this.setState({basketItems: basketItems});
        this.setState({basket: basket});
        this.setState({baskets: fetched});
        console.log(`all fetched...`);
        this.setState({fetched:true});

    }

    render() {
        const {fetched} = this.state;
        if (!fetched){
            return null;
        }

        const tableHeader =  <tr>
            <th>Id</th>
            <th>Product Name</th>
            <th>Count</th>
        </tr>
        console.log(this.state.basketItems);
        let tableObjects = this.state.basketItems.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{this.state.products.get(p.productId)}</th>
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