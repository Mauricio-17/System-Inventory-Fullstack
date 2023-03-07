package com.mauricio.inventory.employee;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping
    public List<Employee> getAllEmployees( ){
        return employeeService.getAllItems( );
    }

    @GetMapping("/{id}")
    public Employee getEquipment(@PathVariable(value = "id") Long id){
        return employeeService.getItem(id);
    }

    @PostMapping
    public ResponseEntity<Void> addEmployee(@Valid @RequestBody Employee employee ){
        employeeService.addItem(employee );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployee(@RequestBody Employee employee, @PathVariable(value = "id") Long id ){
        employeeService.updateItem(employee, id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(value = "id") Long id ){
        employeeService.removeItem(id );
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
