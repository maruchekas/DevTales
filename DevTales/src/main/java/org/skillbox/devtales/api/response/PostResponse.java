package org.skillbox.devtales.api.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.skillbox.devtales.model.User;
import org.skillbox.devtales.model.data.ModerationStatus;

@Setter
  @Getter
  public class PostResponse {

    private int id;
    private int isActive;
    private String title;
    private String text;
    private ModerationStatus moderationStatus;
    private LocalDateTime dateTime;
    private int viewCount;
    private User moderator;
    private User user;

  }
