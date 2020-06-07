import * as React from "react";
import GoogleLogin from "react-google-login";
import API_ENDPOINT from "../configuration/Constants";
import {Redirect} from "react-router-dom";



export class LoginView extends React.Component {


    redirectToTarget () {
        this.props.history.push(`/target`)
    };

    render () {
        return (
         <Redirect to='/authenticate/google'/>
        )
    }

    renderRedirect() {

    }
}