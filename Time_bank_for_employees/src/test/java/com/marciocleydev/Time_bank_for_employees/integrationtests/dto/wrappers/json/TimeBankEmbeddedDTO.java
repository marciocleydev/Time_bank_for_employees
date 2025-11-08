package com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.TimeBankDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


public class TimeBankEmbeddedDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("timeBanks")
    private List<TimeBankDTO> timeBanks;

    public TimeBankEmbeddedDTO() {
    }
    public List<TimeBankDTO> getTimeBanks() {
        return timeBanks;
    }
    public void setTimeBanks(List<TimeBankDTO> timeBanks) {
        this.timeBanks = timeBanks;
    }
}
