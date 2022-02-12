package org.skillbox.devtales.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EditProfileWithPhotoRequest implements EditRequest{

    private MultipartFile photo;
    private String name;
    private String email;
    private String password;
    private int removePhoto;

}
