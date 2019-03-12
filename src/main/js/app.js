import '../resources/static/main.css'
const React = require('react');
const ReactDom = require('react-dom');
const client = require('./client');

class App extends React.Component{
    constructor(props){
        super(props);
        this.state = {orders:[]};
    }

    componentDidAmount(){
        client({method:'GET',path:'/api/orders'}).done(
            response => {this.setState({orders:response.entity._embedded.orders})}
        );
    }

    render(){
        return(
            <OrderList orders = {this.state.orders}/>
        )
    }
}

class OrderList extends React.Component{
    render(){
        const orders = this.props.orders;
            return(
                <table>
                    <tbody>
                        <tr>
                            <th>Description</th>
                            <th>Status</th>
                        </tr>
                        {orders}
                    </tbody>
                </table>
            )
    }
}

class Order extends React.Component{
    render(){
        return(
            <tr>
                <td>{this.props.order.description}</td>
                <td>{this.props.order.status}</td>
            </tr>
        )
    }
}


ReactDom.render(
    <App/>,
    document.getElementById('react')
)