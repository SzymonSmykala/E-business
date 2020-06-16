import React, {Component} from "react";
import {Button, Form, FormGroup, Input, Label} from "reactstrap";
import {LoginService} from "../services/LoginService";

export class RegisterView extends Component {

    loginService: LoginService;

    constructor() {
        super();
        this.loginService = new LoginService();
        this.state = {email: "", password: ""}
    }


    render(){

        console.log("Regisre view")
        return <div>
            REGISTER PAGE
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
            <Button onClick={() => this.handleSubmit()}>Submit</Button>
            </div>
    }

    handleEmailChange(x) {
        const email = x.target.value;
        this.setState({email: email})
    }

    handlePasswordChange(x) {
        const password = x.target.value;
        this.setState({password: password});
    }

    handleSubmit() {
        const {email, password} = this.state;
        this.loginService.register(email, password).then((x) => {console.log(x);})
    }
}