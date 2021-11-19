package org.skillbox.devtales.api.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class TagResponse {

  private String name;
  private double weight;

}
