import * as React from "react";
import {Redirect} from "react-router-dom";


export class LoginCallbackView extends React.Component {

    render () {
        let query = this.props.location.search;
        console.log(query);
        fetch("http://localhost:9000/authenticate/google/" + query)
            .then(x => console.log(x));

        return (
            <div>DUPA</div>
        )
    }

}