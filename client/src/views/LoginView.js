import * as React from "react";
import {Component} from "react";
import GoogleLogin from "react-google-login";
import Cookies from 'js-cookie'
import {Redirect} from "react-router-dom";
import {LoginService} from "../services/LoginService";
import FacebookLogin from 'react-facebook-login';

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
            <FacebookLogin
                appId="3098923260145945"
                autoLoad={false}
                size="small"
                buttonText="Login"
                fields="name,email,picture"
                onClick={() => this.fbButtonClicked()}
                callback={x => this.handleFacebookLoginSuccess(x)} />
        </div>
    }

    handleFacebookLoginSuccess(response) {
       console.log(response);
       Cookies.set('loginProvider', 'facebook');
       Cookies.set('token', response.accessToken);
       Cookies.set('loginInfo', response);
       console.log('calling loginEnpoint..')
        this.loginService.login().then(this.setState({redirect: true}));
    }

    async handleLoginSuccess(response) {
        console.log(response);
        Cookies.set('loginProvider', 'google');
        Cookies.set('token', response.accessToken);
        Cookies.set('loginInfo', response);
        console.log('calling loginEnpoint..')
        const loginResponse = await this.loginService.login();
        Cookies.set('jwtToken', loginResponse.jwtToken);
        this.setState({redirect: true});
    }

    fbButtonClicked() {

    }
}