package payroll;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository){
        return args -> {
            log.info("Preloading" + employeeRepository.save(new Employee("Bilbo","Baggins","chef")));
            log.info("Preloading" + employeeRepository.save(new Employee("Martina","Klaskova","wife")));
//            orderRepository.save(new Order("Macbook Pro",Status.COMPLETED));
//            orderRepository.save(new Order("iPhone",Status.IN_PROGRESS));
//            orderRepository.findAll().forEach(order -> {
//                log.info("Preloaded " + order);
//            });
        };
    }
}
