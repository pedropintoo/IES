package ies.lab.restapi.restapi_mysql;


import java.util.List;

import ies.lab.restapi.restapi_mysql.exceptionHandlers.ResourceNotFoundException;

public interface EmployeeService {
    Employee createEmployee(Employee employee);

    Employee getEmployeeById(Long employeeId) throws ResourceNotFoundException;

    Employee getEmployeeByEmail(String email) throws ResourceNotFoundException;

    List<Employee> getAllEmployees();

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Long employeeId);
}
