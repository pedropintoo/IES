package ies.lab.restapi.restapi_mysql;

import org.springframework.stereotype.Service;

import ies.lab.restapi.restapi_mysql.exceptionHandlers.ResourceNotFoundException;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
        }

    @Override
    public Employee getEmployeeById(Long employeeId) throws ResourceNotFoundException {
        Employee optionalEmployee = employeeRepository.findById(employeeId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + employeeId + " not found"));
        return optionalEmployee;
    }

    @Override
    public Employee getEmployeeByEmail(String email) throws ResourceNotFoundException {
        Employee optionalEmployee = employeeRepository.findByEmail(email)
                                    .orElseThrow(() -> new ResourceNotFoundException("Employee with email " + email + " not found"));
        return optionalEmployee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        Employee existingEmployee = employeeRepository.findById(employee.getId()).get();
        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}