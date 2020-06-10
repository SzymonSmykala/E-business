import * as React from "react";
import {Component} from "react";
import GoogleLogin from "react-google-login";
import Cookies from 'js-cookie'
import {Redirect} from "react-router-dom";
import {LoginService} from "../services/LoginService";

export class LoginView extends Component {


    loginService: LoginService;

    constructor() {
        super();
        this.loginService = new LoginService();
        this.state = {redirect:false}
    }

    render() {

        const {redirect} = this.state;

        if (redirect) {
            return <Redirect to="/products"/>
        }

        return <div>
            <GoogleLogin
                clientId="66836125204-uhmekvs9ssuj9ugqimqbt9f7g5cnc74j.apps.googleusercontent.com"
                buttonText="Login"
                onSuccess={(x) => this.handleLoginSuccess(x)}
                onFailure={(x) => console.log(x)}
                cookiePolicy={'single_host_origin'}
            />
        </div>
    }

    handleLoginSuccess(response) {
        Cookies.set('token', response.accessToken)
        Cookies.set('loginInfo', response)
        this.loginService.login().then(this.setState({redirect: true}));
    }
}