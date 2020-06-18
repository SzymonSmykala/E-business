import * as React from "react";
import {Component} from "react";
import {Table} from "reactstrap";
import {CategoryService} from "../services/CategoryService";

export class CategoriesView extends Component {

    categoryService: CategoryService;

    constructor() {
        super();
        this.categoryService = new CategoryService();
        this.state = {categories: []};
    }

    async componentDidMount(): void {
        let fetched = await this.categoryService.fetchAll();
        this.setState({categories: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>Name</th>
        </tr>

        let tableObjects = this.state.categories.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.name}</th>
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