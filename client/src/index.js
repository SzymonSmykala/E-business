import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter, Route} from 'react-router-dom';
import {ProductsView} from "./views/ProductsView";
import {CategoriesView} from "./views/CategoriesView";
import {BasketView} from "./views/BasketView";
import {PromosView} from "./views/PromosView";
import {PaymentsView} from "./views/PaymentsView";
import {OrderView} from "./views/OrderView";
import {BasketItemsView} from "./views/BasketItemsView";
import {ProductQuestionView} from "./views/ProductQuestionView";
import {FavoriteItemsView} from "./views/FavoriteItemsView";
import {LoginView} from "./views/LoginView";
import {PlaceOrderView} from "./views/PlaceOrderView";
import {MainView} from "./views/MainView";
import {RegisterView} from "./views/RegisterView";

const routing = (
    <BrowserRouter>
        <div>
            <Route exact path="/products" component={ProductsView}/>
            <Route exact path="/categories" component={CategoriesView}/>
            <Route exact path="/baskets" component={BasketView}/>
            <Route exact path="/promos" component={PromosView}/>
            <Route exact path="/payments" component={PaymentsView}/>
            <Route exact path="/orders" component={OrderView}/>
            <Route exact path="/basketItems" component={BasketItemsView}/>
            <Route exact path="/productQuestions" component={ProductQuestionView}/>
            <Route exact path="/favoriteItems" component={FavoriteItemsView}/>
            <Route exact path="/login" component={LoginView}/>
            <Route exact path="/placeOrder/:orderId" component={PlaceOrderView}/>
            <Route exact path="/register" component={RegisterView}/>
            <Route exact path="/" component={MainView}/>
        </div>
    </BrowserRouter>
);

ReactDOM.render(
    routing,
    document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
