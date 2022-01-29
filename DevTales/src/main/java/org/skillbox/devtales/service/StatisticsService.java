package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.StatisticsResponse;
import org.skillbox.devtales.exception.UnAuthorisedUserException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface StatisticsService {

    StatisticsResponse getMyStatistics(Principal principal) throws UnAuthorisedUserException;

    StatisticsResponse getAllStatistics(Principal principal) throws UnAuthorisedUserException;

}
