package payroll;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderResourceAssembler orderResourceAssembler;
    OrderController(OrderRepository orderRepository, OrderResourceAssembler orderResourceAssembler){
        this.orderRepository = orderRepository;
        this.orderResourceAssembler = orderResourceAssembler;
    }

    @GetMapping("/orders")
    Resources<Resource<Order>> all() {

        List<Resource<Order>> orders = orderRepository.findAll().stream()
                .map(orderResourceAssembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/id")
    Resource<Order> one(@PathVariable Long id){
        return orderResourceAssembler.toResource(
                orderRepository.findById(id)
                        .orElseThrow(()-> new OrderNotFoundException(id)));
    }

    @PostMapping("/orders")
    ResponseEntity<Resource<Order>> newOrder (@RequestBody Order order){
        order.setStatus(Status.IN_PROGRESS);
        Order newOrder =orderRepository.save(order);

        return ResponseEntity.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
                .body(orderResourceAssembler.toResource(newOrder));

    }

    @DeleteMapping("/orders/{id}/cancel")
    ResponseEntity<ResourceSupport> cancel(@PathVariable Long id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(orderResourceAssembler.toResource(orderRepository.save(order)));
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed","the method you are using is not allowed for this order"+order.getStatus()+" status"));
    }

    @PutMapping("/orders/{id}/complete")
    ResponseEntity<ResourceSupport> complete(@PathVariable Long id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new OrderNotFoundException(id));
        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(orderResourceAssembler.toResource(orderRepository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed"," You cant't complete this order with "+order.getStatus()+ " status"));
    }

}
