package org.skillbox.devtales.api.response;

import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.model.User;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class AuthCheckResponse {

  private boolean result;
  private User user;

}
