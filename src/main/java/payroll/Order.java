package payroll;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "CUSTOMER_ORDERS")
public class Order {
    private @Id @GeneratedValue Long id;
    private String description;
    private Status status;

    Order(String description, Status status){
        this.description = description;
        this.status = status;
    }

    Order(){}
}
