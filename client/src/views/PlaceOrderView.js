import React, {Component} from "react";
import {PaymentService} from "../services/PaymentService";
import {OrderService} from "../services/OrderService";
import {Button} from "reactstrap";
import {BasketService} from "../services/BasketService";
import {BasketItemService} from "../services/BasketItemService";
import {ProductService} from "../services/ProductService";

export class PlaceOrderView extends Component {

    paymentService: PaymentService;
    orderService: OrderService;
    basketService: BasketService;
    basketItemService: BasketItemService;
    productService: ProductService;

    constructor(props) {
        super(props);
        this.paymentService = new PaymentService();
        this.orderService = new OrderService();
        this.basketService = new BasketService();
        this.basketItemService = new BasketItemService();
        this.productService = new ProductService();
        this.state = {"orderId": this.props.match.params.orderId, fetched: false, products: new Map(), basketItems: [], total: 0};
    }

    componentDidMount = async () => {
        let {orderId} = this.state;
        let order = await this.orderService.getOrder(orderId);
        let payment = await this.paymentService.getPayment(order.paymentId);
        let products = await this.productService.fetchAll();
        products.map(p => this.state.products.set(p.id, p));
        let basketId = order.basketId;
        let basketItems = await this.basketItemService.fetchBasketItemsByBasketId(basketId);
        this.setState({basketItems: basketItems});
        this.setState({paymentStatus : payment.status});
        this.setState({paymentId: payment.id})
        this.setState({fetched: true});
    };

    render(){
        let {paymentStatus, fetched, total} = this.state;
        if (!fetched)
        {
            return null;
        }
        var payStatusButton;
        if (paymentStatus !== "completed"){
            payStatusButton = <Button color="danger" >Not Paid</Button>
        }else{
            payStatusButton = <Button color="success">Paid</Button>
        }

        return (<div className="container">

            <h1>Order number: {this.state.orderId}</h1>
                <table className="table table-striped table-hover table-bordered">
                    <tbody>
                    <tr>
                    </tr>
                    <tr>
                        <th colSpan="2"><span className="pull-right">Total</span></th>
                        <th>$ {this.calculateSum()}</th>
                    </tr>
                    <tr>
                        <td>{payStatusButton}</td>
                        <td colSpan="3"><Button color="primary" onClick={() => this.payHandle()}>Pay</Button></td>
                    </tr>
                    </tbody>
                </table>

        </div>)

    }

    calculateSum = () =>{
        let {basketItems, products} = this.state;
        let sum = 0;
        basketItems.map( item => sum += products.get(item.productId).price * item.count);
        return sum;
    }

    payHandle = async () => {
        let {paymentId} = this.state;
        let payment = await this.paymentService.completePayment(paymentId)
        this.setState({paymentStatus: payment.status});
        this.forceUpdate()
    }
}