package com.azerite.log_analyzer.controller;

import com.azerite.log_analyzer.dto.ReportActorDTO;
import com.azerite.log_analyzer.service.LogService;
import com.azerite.log_analyzer.service.converter.ReportActorConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LogController {

    private final LogService logService;
    private final ReportActorConverter reportActorConverter;

    @Autowired
    public LogController(LogService logService, ReportActorConverter reportActorConverter) {
        this.logService = logService;
        this.reportActorConverter = reportActorConverter;
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
    @GetMapping("/v1/getReportActors")
    public List<ReportActorDTO> getReportActors(@RequestParam String code) {
        return reportActorConverter.convert(logService.getResponseFromAPI(code, "{masterData{actors(type: \\\"Player\\\") { name } } }"));
    }
}