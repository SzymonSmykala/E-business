let API_ENDPOINT;

if(process.env.NODE_ENV === 'development') {
    API_ENDPOINT = 'http://localhost:9000'
}

export default API_ENDPOINT;