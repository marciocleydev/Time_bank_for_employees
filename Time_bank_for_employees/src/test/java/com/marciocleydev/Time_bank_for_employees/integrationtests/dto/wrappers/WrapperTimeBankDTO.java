package com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marciocleydev.Time_bank_for_employees.integrationtests.dto.wrappers.json.TimeBankEmbeddedDTO;

import java.io.Serial;
import java.io.Serializable;

public class WrapperTimeBankDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private TimeBankEmbeddedDTO embedded;

    public WrapperTimeBankDTO() {
    }

    public TimeBankEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(TimeBankEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}
