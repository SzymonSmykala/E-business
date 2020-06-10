import {Component, default as React} from "react";
import {LoginView} from "./LoginView";
import {NavBarView} from "./NavBarView";


export class MainView extends Component{

    render() {
        return (
            <div>
            <NavBarView/>
        <div style={{display: 'flex',  justifyContent:'center', alignItems:'center', height: '100vh', marginLeft: '10vh', marginRight: '10vh'}}>
            <LoginView></LoginView>
        </div>
            </div>
        )

    }
}