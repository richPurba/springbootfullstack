const React = require('react');
const ReactDom = require('react-dom');
const client = require('./client');

class App extends React.Component{
    constructor(props){
        super(props);
        this.state = {employee:[]}
    }

    componentDidAmount(){
        client({method:'GET',path:'/api/employees'}).done(
            response => {this.setState({employees:response.entity._embedded.employees})}
        );
    }

    render(){
        return(
            <EmployeeList employees = {this.state.employees}/>
        )
    }
}