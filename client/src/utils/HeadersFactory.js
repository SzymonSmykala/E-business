import Cookies from 'js-cookie'

export class HeadersFactory{

    create(): Headers{
        return new Headers({'token': Cookies.get('token'), 'Content-Type': 'application/json'})
    }

}