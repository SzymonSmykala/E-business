import React, {Component} from "react";
import {
    Collapse,
    Navbar,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
} from 'reactstrap';


export class NavBarView extends Component {

    render() {
        return (
            <div>
                <Navbar color="light" light expand="md">
                    <NavbarBrand href="/">Shopify</NavbarBrand>
                    {/*<NavbarToggler onClick={toggle} />*/}
                    <Collapse isOpen={false} navbar>
                        <Nav className="mr-auto" navbar>
                            <NavItem>
                                <NavLink href="/products">Products</NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink href="/baskets">Basket</NavLink>
                            </NavItem>
                        </Nav>
                    </Collapse>
                </Navbar>
            </div>
        );

    }

}