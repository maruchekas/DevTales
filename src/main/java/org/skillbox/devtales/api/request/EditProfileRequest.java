package org.skillbox.devtales.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EditProfileRequest implements EditRequest{

    private String photo;
    private String name;
    private String email;
    private String password;
    private int removePhoto;
}
