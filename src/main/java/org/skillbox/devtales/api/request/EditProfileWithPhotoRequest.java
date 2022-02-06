package org.skillbox.devtales.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileWithPhotoRequest implements EditRequest{

    private MultipartFile photo;
    private String name;
    private String email;
    private String password;
    private int removePhoto;

}
