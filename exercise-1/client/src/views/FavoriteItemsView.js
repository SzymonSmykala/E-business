import * as React from "react";
import {Component} from "react";
import {Table} from "reactstrap";
import {FavoriteItemService} from "../services/FavoriteItemService";

export class FavoriteItemsView extends Component {

    favoriteItemService: FavoriteItemService;

    constructor() {
        super();
        this.favoriteItemService = new FavoriteItemService();
        this.state = {items: []};
    }

    async componentDidMount(): void {
        let fetched = await this.favoriteItemService.fetchAll();
        this.setState({items: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>User id</th>
            <th>Product Id</th>
        </tr>

        let tableObjects = this.state.items.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.userId}</th>
                <th>{p.productId}</th>
            </tr>

        ));
        return <div>
            <div style={{display: 'flex',  justifyContent:'center', alignItems:'center', height: '100vh', marginLeft: '10vh', marginRight: '10vh'}}>
                <Table>
                    <thead>
                    {tableHeader}
                    </thead>
                    <tbody>
                    {tableObjects}
                    </tbody>
                </Table>
            </div>

        </div>
    }

}