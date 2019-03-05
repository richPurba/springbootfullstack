package payroll;

public class OrderNotFoundException extends RuntimeException {
    OrderNotFoundException(Long id){
        super("COuldn't find the order with id "+id);
    }
}
