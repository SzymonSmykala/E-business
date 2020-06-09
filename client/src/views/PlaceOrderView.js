import React, {Component} from "react";
import {PaymentService} from "../services/PaymentService";
import {OrderService} from "../services/OrderService";
import {Button} from "reactstrap";

export class PlaceOrderView extends Component {

    paymentService: PaymentService;
    orderService: OrderService;

    constructor(props) {
        super(props);
        this.paymentService = new PaymentService();
        this.orderService = new OrderService();
        this.state = {"orderId": this.props.match.params.orderId, fetched: false}
    }

    componentDidMount = async () => {
        let {orderId} = this.state;
        let order = await this.orderService.getOrder(orderId);
        let payment = await this.paymentService.getPayment(order.paymentId);
        this.setState({paymentStatus : payment.status});
        this.setState({paymentId: payment.id})
        this.setState({fetched: true});
    };

    render(){
        let {paymentStatus, fetched} = this.state;
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
                        <th>Item</th>
                        <th>QTY</th>
                        <th>Unit Price</th>
                        <th>Total Price</th>
                    </tr>
                    <tr>
                        <td>Awesome Product</td>
                        <td>1 <a href="#">X</a></td>
                        <td>£250.00</td>
                        <td>£250.00</td>
                    </tr>
                    <tr>
                        <th colSpan="3"><span className="pull-right">Sub Total</span></th>
                        <th>£250.00</th>
                    </tr>
                    <tr>
                        <th colSpan="3"><span className="pull-right">VAT 20%</span></th>
                        <th>£50.00</th>
                    </tr>
                    <tr>
                        <th colSpan="3"><span className="pull-right">Total</span></th>
                        <th>£300.00</th>
                    </tr>
                    <tr>
                        <td>{payStatusButton}</td>
                        <td colSpan="3"><Button color="primary" onClick={() => this.payHandle()}>Pay</Button></td>
                    </tr>
                    </tbody>
                </table>

        </div>)

    }

    payHandle = async () => {
        let {paymentId} = this.state;
        let payment = await this.paymentService.completePayment(paymentId)
        this.setState({paymentStatus: payment.status});
        this.forceUpdate()
    }
}