package org.skillbox.devtales.service;

import org.skillbox.devtales.api.response.TagResponse;
import org.springframework.stereotype.Service;

@Service
public class TagService {

  public TagResponse getTagInfo(){
    TagResponse tagResponse = new TagResponse();
    tagResponse.setName("JAVA");
    tagResponse.setWeight(0.9);

    return tagResponse;
  }

}
