package org.skillbox.devtales.controller;

import lombok.AllArgsConstructor;
import org.skillbox.devtales.api.response.StatisticsResponse;
import org.skillbox.devtales.exception.UnAuthorisedUserException;
import org.skillbox.devtales.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/statistics")
public class ApiStatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/all")
    public ResponseEntity<StatisticsResponse> getAllStatistics(Principal principal) throws UnAuthorisedUserException {
        return new ResponseEntity<>(statisticsService.getAllStatistics(principal), HttpStatus.OK);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatisticsResponse> getMyStatistics(Principal principal) throws UnAuthorisedUserException {
        return new ResponseEntity<>(statisticsService.getMyStatistics(principal), HttpStatus.OK);
    }
}
