package com.marciocleydev.Time_bank_for_employees.mapper;

import com.marciocleydev.Time_bank_for_employees.DTO.TimeSheetDTO;
import com.marciocleydev.Time_bank_for_employees.entities.TimeSheet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")//Ela diz para o MapStruct gerar um mapper que é reconhecido como um bean do Spring, ou seja, você pode injetar esse mapper usando @Autowired
public interface TimeSheetMapper {

    TimeSheetDTO toDTO(TimeSheet timeSheet);

    TimeSheet toEntity(TimeSheetDTO timeSheetDTO);

    List<TimeSheetDTO> toDTOList(List<TimeSheet> timeSheetList);

    List<TimeSheet> toEntityList(List<TimeSheetDTO> timeSheetDTOList);
}
