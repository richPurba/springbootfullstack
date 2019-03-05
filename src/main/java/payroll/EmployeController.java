package payroll;

import org.apache.catalina.loader.ResourceEntry;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
class EmployeController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeResourceAssembler employeeResourceAssembler;


    EmployeController(EmployeeRepository employeeRepository, EmployeeResourceAssembler employeeResourceAssembler){
        this.employeeRepository = employeeRepository;
        this.employeeResourceAssembler = employeeResourceAssembler;
    }

    //Aggregate root
    @GetMapping("/employees")
    Resources<Resource<Employee>> all(){
        List<Resource<Employee>> employees = employeeRepository.findAll().stream()
                .map(employeeResourceAssembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(employees, linkTo(methodOn(EmployeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
        Resource<Employee> resource = employeeResourceAssembler.toResource(employeeRepository.save(newEmployee));
        return ResponseEntity.created(new URI(resource.getId().getHref()))
                .body(resource);
    }

    //Single Item
    @GetMapping("/employees/{id}")
    Resource<Employee> one(@PathVariable Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return employeeResourceAssembler.toResource(employee);
    }

    @PutMapping("employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException{
        Employee updatedEmployee = employeeRepository.findById(id).map(
                            employee -> {
                                employee.setName(employee.getName());
                                employee.setRole(employee.getRole());
                                return employeeRepository.save(employee);
                            }
                    ).orElseGet(()->{
                                newEmployee.setId(id);
                                return employeeRepository.save(newEmployee);
        });
        Resource<Employee> resource = employeeResourceAssembler.toResource(updatedEmployee);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);

    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
