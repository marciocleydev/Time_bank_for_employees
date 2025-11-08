package com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers.xml_yaml;

import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.TimeBankDTO;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PageModelTimeBank implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public List<TimeBankDTO> content;

    public PageModelTimeBank() {
    }
    public List<TimeBankDTO> getContent() {
        return content;
    }
    public void setContent(List<TimeBankDTO> content) {
        this.content = content;
    }
}
