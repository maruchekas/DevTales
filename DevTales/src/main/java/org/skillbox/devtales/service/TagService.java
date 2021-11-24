package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.TagResponse;
import org.springframework.stereotype.Service;

@Service
public class TagService {

  public TagResponse getTag(){
    TagResponse tagResponse = new TagResponse();
    tagResponse.setName("Java");
    return tagResponse;
  }

}
