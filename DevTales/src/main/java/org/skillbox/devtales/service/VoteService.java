package org.skillbox.devtales.service;

import org.skillbox.devtales.api.request.VoteRequest;
import org.skillbox.devtales.api.response.CommonResponse;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface VoteService {

    CommonResponse castVote(VoteRequest voteRequest, int value, Principal principal);

}
