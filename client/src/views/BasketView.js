import * as React from "react";
import {Component} from "react";
import {Button, Table} from "reactstrap";
import {BasketService} from "../services/BasketService";
import {ProductService} from "../services/ProductService";
import {BasketItemService} from "../services/BasketItemService";
import {NavBarView} from "./NavBarView";
import {PaymentService} from "../services/PaymentService";
import {Redirect} from "react-router-dom";
import {OrderService} from "../services/OrderService";

export class BasketView extends Component {

    basketService: BasketService;
    productService: ProductService;
    basketItemsService: BasketItemService;
    paymentService: PaymentService;
    orderService: OrderService;
    defaultUserId = 1;

    constructor() {
        super();
        this.basketService = new BasketService();
        this.paymentService = new PaymentService();
        this.basketItemsService = new BasketItemService();
        this.productService = new ProductService();
        this.orderService = new OrderService();
        this.state = {baskets: [], products: new Map(), basketItems: [], fetched: false, orderReady: false};
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
        const {fetched, orderReady, orderId, products} = this.state;
        if (!fetched){
            return null;
        }

        if (orderReady){
            return  <Redirect to={"/placeOrder/" + orderId}/>
        }

        const tableHeader =  <tr>
            <th>Id</th>
            <th>Product Name</th>
            <th>Count</th>
            <th>Price</th>
        </tr>
        console.log(this.state.basketItems);
        let tableObjects = this.state.basketItems.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{this.state.products.get(p.productId).name}</th>
                <th>{p.count}</th>
                <th>{p.count * products.get(p.productId).price}</th>
            </tr>

        ));
        return <div>
            <NavBarView/>
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
            <div style={{float: 'right', marginBottom: '20vh', marginRight: '10vh'}}>
                 <Button  color="primary" size="lg" onClick={() => this.processCheckout()}>Place Order</Button>
            </div>
        </div>
    }

    processCheckout = async () => {
        let payment = await this.paymentService.createNewPayment();
        let order = await this.orderService.create(this.state.basket.id, payment.id);
        this.setState({orderId: order.id})
        this.setState({orderReady: true})
    }
}