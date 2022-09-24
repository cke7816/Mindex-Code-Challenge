package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String reportingSructureIdURL;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingSructureIdURL = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        Employee testEmployee1 = new Employee();
        testEmployee1.setFirstName("Joe");
        testEmployee1.setLastName("Citizen");
        testEmployee1.setDepartment("Engineering");
        testEmployee1.setPosition("Manager");
        List<Employee> directReportList = new ArrayList<>();
        directReportList.add(createdEmployee);
        testEmployee1.setDirectReports(directReportList);

        createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();

        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Mary");
        testEmployee2.setLastName("Major");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Manager");
        directReportList = new ArrayList<>();
        directReportList.add(createdEmployee);
        testEmployee2.setDirectReports(directReportList);

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();

        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingSructureIdURL, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();

        assertEquals(2, reportingStructure.getNumberOfReports());
    }
}
