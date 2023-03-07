package com.mauricio.inventory.employee;

import com.mauricio.inventory.exceptions.BadRequestException;
import com.mauricio.inventory.exceptions.ResourceNotFoundException;
import com.mauricio.inventory.role.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public List<Employee> getAllItems( ){
        return employeeRepository.findAll();
    }


    public Employee getItem(Long id ){
        Employee foundEmployee = employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Empleado con el ID %s no encontrada", id))
        );
        return foundEmployee;
    }

    public void addItem(Employee employee ){
        if(employeeRepository.existsEmail(employee.getEmail())){
            throw new BadRequestException("La contraseña o el email ya se encuentran registrados");
        }

        employeeRepository.save(employee);
    }

    public void updateItem(Employee employee, Long id ){
        employeeRepository.findById(id).map(emp -> {
            emp.setName(employee.getName());
            emp.setLastName(employee.getLastName());
            emp.setStatus(employee.getStatus());
            emp.setEmail(employee.getEmail());
            return employeeRepository.save(emp);
        }).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Empleado con el ID %s no encontrada", id))
        );
    }

    public void removeItem(Long id ){
        Optional<Employee> foundEmployee = employeeRepository.findById(id);
        if (foundEmployee.isEmpty()){
            throw new ResourceNotFoundException(String.format("Empleado con el ID %s no encontrada", id));
        }
        employeeRepository.delete(foundEmployee.get());
    }
}
