import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter, Route} from 'react-router-dom';
import {ProductsView} from "./views/ProductsView";
import {CategoriesView} from "./views/CategoriesView";
import {BasketView} from "./views/BasketView";


const routing = (
    <BrowserRouter>
        <div>
            <Route exact path="/products" component={ProductsView}/>
            <Route exact path="/categories" component={CategoriesView}/>
            <Route exact path="/baskets" component={BasketView}/>
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
