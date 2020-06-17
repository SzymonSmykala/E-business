import {Component, default as React} from "react";
import {LoginView} from "./LoginView";
import {NavBarView} from "./NavBarView";
import {Button, Form, FormGroup, Input, Label} from "reactstrap";
import {LoginService} from "../services/LoginService";
import Cookies from 'js-cookie'

export class MainView extends Component{

    loginService: LoginService;

    constructor() {
        super();
        this.loginService = new LoginService();

        this.state = {email: "", password: ""}
    }


    render() {
        return (
            <div>
            <NavBarView/>
        <div style={{display: 'flex',  justifyContent:'center', alignItems:'center', height: '100vh', marginLeft: '10vh', marginRight: '10vh'}}>
            <div>
                <Form>
                    <FormGroup>
                        <Label for="exampleEmail">Email</Label>
                        <Input type="email" name="email" id="exampleEmail" placeholder="email@email.com" onChange={(x) => this.handleEmailChange(x)} />
                    </FormGroup>
                    <FormGroup>
                        <Label for="examplePassword">Password</Label>
                        <Input type="password" name="password" id="examplePassword" placeholder="***"  onChange={(x) => this.handlePasswordChange(x)} />
                    </FormGroup>
                </Form>

                <Button onClick={() => this.handleLoginClick()}>Login using username and password</Button>

            </div>
            <LoginView></LoginView>
        </div>
            </div>
        )

    }

    handleEmailChange(x) {
        const email = x.target.value;
        this.setState({email: email})
    }

    handlePasswordChange(x) {
        const password = x.target.value;
        this.setState({password: password});
    }


    handleLoginClick() {
        const{email, password} = this.state;
        this.loginService.loginWithEmailAndPassword(email, password).then(x => Cookies.set("jwtToken", x.jwtToken));
    }
}