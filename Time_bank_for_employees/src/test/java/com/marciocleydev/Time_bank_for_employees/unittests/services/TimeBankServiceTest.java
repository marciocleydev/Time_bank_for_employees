package com.marciocleydev.Time_bank_for_employees.unittests.services;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeBankDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.entities.TimeBank;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.mapper.TimeBankMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.EmployeeRepository;
import com.marciocleydev.Time_bank_for_employees.repositories.TimeBankRepository;
import com.marciocleydev.Time_bank_for_employees.services.TimeBankService;
import com.marciocleydev.Time_bank_for_employees.unittests.mocks.MockEmployee;
import com.marciocleydev.Time_bank_for_employees.unittests.mocks.MockTimeBank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TimeBankServiceTest {

    MockTimeBank timeBankInput;

    @InjectMocks
    private TimeBankService service;
    @Mock
    private TimeBankRepository repository;
    @Mock
    private TimeBankMapper mapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private MockEmployee employeeInput;

    @BeforeEach
     void setUp() {
        timeBankInput = new MockTimeBank();
        employeeInput = new MockEmployee();
    }

    @Test
    void getBalance() {
        Employee employee = employeeInput.mockEntity(2);
        TimeBank timeBank = timeBankInput.mockEntity(1);
        employee.setTimeBank(timeBank);

        TimeBankDTO timeBankDTO = timeBankInput.mockDTO(1);

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(mapper.toDTO(employee.getTimeBank())).thenReturn(timeBankDTO);

        var result = service.getBalance(2L);

        assertNotNull(result);
        assertEquals(timeBankDTO, result);
        hasLinkHateoas(result);

        verify(employeeRepository).findById(2L);
        verify(mapper).toDTO(employee.getTimeBank());
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void getBalanceEmployeeNotFound() {
        employeeNotFound();
    }

    @Test
    void addHours() {
        Employee employee = employeeInput.mockEntity(2);
        TimeBank timeBank = timeBankInput.mockEntity(1);
        employee.setTimeBank(timeBank);

        TimeBankDTO timeBankDTO = timeBankInput.mockDTO(1);

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(mapper.toDTO(employee.getTimeBank())).thenReturn(timeBankDTO);

        var result = service.addHours(employee.getId(), 10);

        assertNotNull(result);
        assertEquals(timeBankDTO, result);
        hasLinkHateoas(result);

        verify(employeeRepository).findById(employee.getId());
        verify(employeeRepository).save(employee);
        verify(mapper).toDTO(employee.getTimeBank());
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void addHoursEmployeeNotFound() {
        employeeNotFound();
    }

    @Test
    void removeHours() {
        Employee employee = employeeInput.mockEntity(2);
        TimeBank timeBank = timeBankInput.mockEntity(1);
        employee.setTimeBank(timeBank);

        TimeBankDTO timeBankDTO = timeBankInput.mockDTO(1);

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(mapper.toDTO(employee.getTimeBank())).thenReturn(timeBankDTO);

        var result = service.removeHours(employee.getId(), 10);

        assertNotNull(result);
        assertEquals(timeBankDTO, result);
        hasLinkHateoas(result);

        verify(employeeRepository).findById(employee.getId());
        verify(employeeRepository).save(employee);
        verify(mapper).toDTO(employee.getTimeBank());
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void removeHoursEmployeeNotFound() {
        employeeNotFound();
    }

    @Test
    void create() {
        TimeBank timeBank = timeBankInput.mockEntity(1);
        TimeBankDTO timeBankDTO = timeBankInput.mockDTO(1);

        when(mapper.toEntity(timeBankDTO)).thenReturn(timeBank);
        when(repository.save(timeBank)).thenReturn(timeBank);
        when(mapper.toDTO(timeBank)).thenReturn(timeBankDTO);

        var result = service.create(timeBankDTO); //lembrar sempre de passar aqui o mesmo que ta sendo mockado a cima

        assertNotNull(result);
        assertEquals(timeBankDTO, result);
        hasLinkHateoas(result);

         verify(repository).save(timeBank);
        verify(mapper).toDTO(timeBank);
        verifyNoMoreInteractions(repository, mapper);
    }

    private void employeeNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getBalance(999L));

        verify(employeeRepository).findById(999L);
        verifyNoMoreInteractions(employeeRepository);
    }

    private void hasLinkHateoas(TimeBankDTO dto) {
        boolean hasAddHoursLink = dto.getLinks().stream()
                .anyMatch(link -> "addHours".equals(link.getRel().value())
                        && link.getHref().endsWith("/employees/"+dto.getEmployeeId()+"/timeBank/add?minutes={minutes}")
                        && "POST".equals(link.getType()));

        assertTrue(hasAddHoursLink, "TimeBankDTO should have a addHours link, but it doesn't");

        boolean hasRemoveHoursLink = dto.getLinks().stream()
                .anyMatch(link -> "removeHours".equals(link.getRel().value())
                        && link.getHref().endsWith("/employees/"+dto.getEmployeeId()+"/timeBank/remove?minutes={minutes}")
                        && "POST".equals(link.getType()));

        assertTrue(hasRemoveHoursLink, "TimeBankDTO should have a removeHours link, but it doesn't");

        boolean hasGetBalanceLink = dto.getLinks().stream()
                .anyMatch(link -> "getBalance".equals(link.getRel().value())
                        && link.getHref().endsWith("/employees/"+dto.getEmployeeId()+"/timeBank/balance")
                        && "GET".equals(link.getType()));

        assertTrue(hasGetBalanceLink, "TimeBankDTO should have a getBalance link, but it doesn't");
    }
}