import * as React from "react";
import {Component} from "react";
import GoogleLogin from "react-google-login";
import Cookies from 'js-cookie'

export class LoginView extends Component {

    render() {

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
        console.log(response)
        Cookies.set('loginInfo', response)
    }
}