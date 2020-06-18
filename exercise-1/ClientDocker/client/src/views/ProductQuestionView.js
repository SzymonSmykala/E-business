import * as React from "react";
import {Component} from "react";
import {Table} from "reactstrap";
import {ProductQuestionService} from "../services/ProductQuestionService";


export class ProductQuestionView extends Component {

    productQuestionService: ProductQuestionService;

    constructor() {
        super();
        this.productQuestionService = new ProductQuestionService();
        this.state = {questions: []};
    }

    async componentDidMount(): void {
        let fetched = await this.productQuestionService.fetchAll();
        this.setState({questions: fetched});
    }

    render() {
        const tableHeader =  <tr>
            <th>Id</th>
            <th>Product Id</th>
            <th>Question</th>
        </tr>

        let tableObjects = this.state.questions.map(p => (
            <tr>
                <th>{p.id}</th>
                <th>{p.productId}</th>
                <th>{p.questionContent}</th>
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