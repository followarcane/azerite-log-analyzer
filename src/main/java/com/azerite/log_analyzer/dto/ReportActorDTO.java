package com.azerite.log_analyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportActorDTO {
    private Float gameID;
    private String icon;
    private Integer id;
    private String name;
    private String petOwner;
    private String server;
    private String subType;
    private String type;
}
