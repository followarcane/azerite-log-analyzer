package com.azerite.log_analyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportFightDTO {
    private Float averageItemLevel;
    private Float bossPercentage;
    private Float startTime;
    private Float endTime;
    private Float fightPercentage;
    private Boolean completeRaid;
    private Boolean inProgress;
    private Boolean kill;
    private Integer difficulty;
    private Integer encounterID;
    private Integer friendlyPlayers;
    private Integer id;
    private String name;
}
