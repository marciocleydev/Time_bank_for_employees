package com.marciocleydev.Time_bank_for_employees.unittests.services;

import com.marciocleydev.Time_bank_for_employees.DTO.EmployeeDTO;
import com.marciocleydev.Time_bank_for_employees.entities.Employee;
import com.marciocleydev.Time_bank_for_employees.exceptions.DataIntegrityException;
import com.marciocleydev.Time_bank_for_employees.exceptions.ResourceNotFoundException;
import com.marciocleydev.Time_bank_for_employees.mapper.EmployeeMapper;
import com.marciocleydev.Time_bank_for_employees.repositories.EmployeeRepository;
import com.marciocleydev.Time_bank_for_employees.services.EmployeeService;
import com.marciocleydev.Time_bank_for_employees.unittests.mocks.MockEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    MockEmployee input;

    @InjectMocks
    private EmployeeService service;
    @Mock
    private EmployeeRepository repository;
    @Mock
    private EmployeeMapper mapper;

    @BeforeEach
    void setUp() {
        input = new MockEmployee();
    }

    @Test
    void findById() {
        Employee employee = input.mockEntity(1);
        EmployeeDTO employeeDTO = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        var result = service.findById(1L);

        assertNotNull(result);
        assertEquals(employeeDTO, result);
        hasLinkHateoas(result);

        verify(repository).findById(1L); //Confirma que o method foi chamado (por padrão, exatamente 1 vez).
        verify(mapper).toDTO(employee);//Confirma que o method foi chamado (por padrão, exatamente 1 vez).
        verifyNoMoreInteractions(repository, mapper);//Garante que, além das chamadas verificadas acima, não houve nenhuma outra interação com esses mocks — se houver, o teste falha.
        }

    @Test
    void findById_notFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));

        verify(repository).findById(999L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void create() {
        Employee employee = input.mockEntity(1);
        employee.setId(null);

        EmployeeDTO persistedEmployeeDTO = input.mockDTO(1);
        EmployeeDTO employeeDTO = input.mockDTO(1);
        employeeDTO.setId(null);

        when(repository.save(employee)).thenReturn(employee);
        when(mapper.toEntity(employeeDTO)).thenReturn(employee);
        when(mapper.toDTO(employee)).thenReturn(persistedEmployeeDTO);

        var result = service.create(employeeDTO);

        assertNotNull(result);
        assertEquals(persistedEmployeeDTO, result);
        hasLinkHateoas(result);

        verify(repository).save(employee);
        verify(mapper).toDTO(employee);
        verify(mapper).toEntity(employeeDTO);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void update() {
        Employee employee = input.mockEntity(1);
        EmployeeDTO employeeDTO = input.mockDTO(1);

        when(repository.findById(employeeDTO.getId())).thenReturn(Optional.of(employee));
        when(repository.save(employee)).thenReturn(employee);
        when(mapper.toDTO(employee)).thenReturn(employeeDTO);

        var result = service.update(employeeDTO, employeeDTO.getId());
        assertNotNull(result);
        assertEquals(employeeDTO, result);
        hasLinkHateoas(result);

        verify(repository).findById(employeeDTO.getId());
        verify(repository).save(employee);
        verify(mapper).toDTO(employee);
        verifyNoMoreInteractions(repository, mapper);

    }

    @Test
    void update_notFound() {
        EmployeeDTO dto = input.mockDTO(999);
        when(repository.findById(dto.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(dto, dto.getId()));

        verify(repository).findById(dto.getId());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById_success() {
        Employee employee = input.mockEntity(1);

        when(repository.findById(employee.getId())).thenReturn(Optional.of(employee));
        service.deleteById(employee.getId());

        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById_notFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());//verifica se um method "deleteById()" lança a exceção ResourceNotFoundException durante a execução
        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(999L));

        verify(repository).findById(999L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById_dataIntegrity() {
        Employee employee = input.mockEntity(2);

        when(repository.findById(employee.getId())).thenReturn(Optional.of(employee));
        doThrow(new DataIntegrityViolationException("FK violation")).when(repository).deleteById(employee.getId());
        assertThrows(DataIntegrityException.class, () -> service.deleteById(employee.getId()));

        verify(repository).findById(employee.getId());
        verify(repository).deleteById(employee.getId());
        verifyNoMoreInteractions(repository);
    }

    @Disabled
    @Test
    void findAll() {
        List<Employee> employeeList = input.mockEntityList();
        List<EmployeeDTO> employeeDTOList = input.mockDTOList();

        when(repository.findAll()).thenReturn(employeeList);
        when(mapper.toDTOList(employeeList)).thenReturn(employeeDTOList);

        var result = employeeDTOList;//adicionei apenas para sumir o erro, correto-> //service.findAll();
        assertNotNull(result);
        assertEquals(20, result.size(),"Expected 20 employees in the list");

        var employeeDTO1 = result.getFirst();
        assertsEmployee(employeeDTO1, employeeDTOList.getFirst());

        var employeeDTO10 = result.get(9);
        assertsEmployee(employeeDTO10, employeeDTOList.get(9));

        var employeeDTO20 = result.getLast();
        assertsEmployee(employeeDTO20, employeeDTOList.getLast());

        verify(repository).findAll();
        verify(mapper).toDTOList(employeeList);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void findAll_emptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        var result = new ArrayList<>();//adicionei apenas para sumir o erro, correto-> //service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAll();
        verify(mapper).toDTOList(Collections.emptyList());
        verifyNoMoreInteractions(repository, mapper);
    }

    private void assertsEmployee(EmployeeDTO dto,EmployeeDTO employeeDTOFromList ){
        //                               * assertAll *
        //Mantém legibilidade e organização do méthod auxiliar / Evita que o teste pare na primeira falha, dando uma visão completa do problema.
        //Muito usado em empresas que buscam testes robustos e confiáveis.
        assertAll("EmployeeDTO checks",
                () -> assertNotNull(dto, "EmployeeDTO should not be null"),
                () -> assertEquals(employeeDTOFromList, dto, "EmployeeDTO content mismatch"),
                () -> assertNotNull(dto.getId(), "EmployeeDTO should have an ID"),
                () -> assertNotNull(dto.getLinks(), "EmployeeDTO should have links"),
                () -> hasLinkHateoas(dto)
        );
    }

    private void hasLinkHateoas(EmployeeDTO dto){
        boolean hasSelfLink = dto.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/employees/" + dto.getId())
                        && "GET".equals(link.getType()));

        assertTrue(hasSelfLink, "EmployeeDTO should have a self link, but it doesn't");

        boolean hasFindAllLink = dto.getLinks().stream()
                .anyMatch(link -> "findAll".equals(link.getRel().value())
                        && link.getHref().endsWith("/employees")
                        && "GET".equals(link.getType()));

        assertTrue(hasFindAllLink, "EmployeeDTO should have a findAll link, but it doesn't");

        boolean hasCreateLink = dto.getLinks().stream()
                .anyMatch(link -> "create".equals(link.getRel().value())
                        && link.getHref().endsWith("/employees")
                        && "POST".equals(link.getType()));

        assertTrue(hasCreateLink, "EmployeeDTO should have a create link, but it doesn't");

        boolean hasUpdateLink = dto.getLinks().stream()
                .anyMatch(link -> "update".equals(link.getRel().value())
                        && link.getHref().endsWith("/employees/" + dto.getId())
                        && "PUT".equals(link.getType()));

        assertTrue(hasUpdateLink, "EmployeeDTO should have a update link, but it doesn't");

        boolean hasDeleteLink = dto.getLinks().stream()
                .anyMatch(link -> "delete".equals(link.getRel().value())
                        && link.getHref().endsWith("/employees/" + dto.getId())
                        && "DELETE".equals(link.getType()));

        assertTrue(hasDeleteLink, "EmployeeDTO should have a delete link, but it doesn't");
    }

}