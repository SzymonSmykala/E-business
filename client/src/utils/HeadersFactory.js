import Cookies from 'js-cookie'

export class HeadersFactory{

    create() {
        return {'token': Cookies.get('token'), 'Content-Type': 'application/json', 'loginProvider': Cookies.get('loginProvider'), jwtToken: Cookies.get('jwtToken')}
    }
}