import '../resources/static/main.css'

const React = require('react');
const ReactDom = require('react-dom');
const client = require('./client');

class App extends React.Component{
    constructor(props){
        super(props);
        this.state = {orders:[]};
    }

    componentDidMount(){
        this.handleTheReturn(); // see this https://stackoverflow.com/questions/24842505/reactjs-unexpected-infinite-loop-with-render
    }
    handleTheReturn(){
        fetch('http://localhost:8080/api/orders').then(response =>
       { if(response.ok){
        return response.json();
    } else{
        throw new Error(" end point is not reachable");
    }}).then( 
        data => this.setState({orders:data._embedded.orders})
    )
    }

    render(){
        console.log('from App state');
        console.log(this.state);
        return(
            <OrderList orders = {this.state.orders} />
        
            )
    }
}

class OrderList extends React.Component{
    render(){
        const orders = this.props.orders.map(order=>(
            <Order key={order._links.self.href} order={order}/>
        ));
        console.log(orders);
    
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