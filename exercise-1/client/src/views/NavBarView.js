import React, {Component} from "react";
import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    UncontrolledDropdown,
    DropdownToggle,
    DropdownMenu,
    DropdownItem,
    NavbarText
} from 'reactstrap';
import {LoginView} from "./LoginView";


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
                        <LoginView/>
                    </Collapse>
                </Navbar>
            </div>
        );

    }

}