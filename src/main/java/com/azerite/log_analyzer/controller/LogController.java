package com.azerite.log_analyzer.controller;

import com.azerite.log_analyzer.dto.ReportActorDTO;
import com.azerite.log_analyzer.dto.ReportFightDTO;
import com.azerite.log_analyzer.service.LogService;
import com.azerite.log_analyzer.service.converter.ReportConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LogController {

    private final LogService logService;
    private final ReportConverter reportConverter;

    @Autowired
    public LogController(LogService logService, ReportConverter reportConverter) {
        this.logService = logService;
        this.reportConverter = reportConverter;
    }

    @PostMapping("/v1/getLogs")
    public String getLogs(@RequestParam String code, @RequestBody String additionalQueryPart) {
        return logService.getResponseFromAPI(code, additionalQueryPart);
    }

    /**
     * Handles HTTP GET requests to retrieve report actors.
     *
     * @param code the code parameter used to fetch the report actors
     * @return a list of ReportActorDTO objects containing the report actors' information
     */
    //Find another additionalQuery, it does not feel right.
    @GetMapping("/v1/getReportActors")
    public ResponseEntity<List<ReportActorDTO>> getReportActors(@RequestParam String code) {
        return ResponseEntity.ok(reportConverter.convert(logService.getResponseFromAPI(code, "{masterData{actors(type: \\\"player\\\") { gameID icon petOwner server subType name id type } } }"), "actors", ReportActorDTO.class));
    }

    @GetMapping("/v1/getFightId")
    public ResponseEntity<List<ReportFightDTO>> getFightId(@RequestParam String code) {
        return ResponseEntity.ok(reportConverter.convert(logService.getResponseFromAPI(code, "{ fights(killType:Encounters) { id name encounterID } }"), "fights", ReportFightDTO.class));
    }

    //Buraya bi fightID ve player ismi girmeliyiz. kimin castleri?   { events(dataType: Casts) { data } }
    //FightID almak icin ->  { fights { id name encounterID } }
    //Damage Done Chart -> { events(fightIDs: [10], sourceID: 15, dataType: DamageDone) { data nextPageTimestamp } }
}