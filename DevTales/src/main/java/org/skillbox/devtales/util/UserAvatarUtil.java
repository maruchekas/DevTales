package org.skillbox.devtales.util;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.skillbox.devtales.config.Constants;

import java.util.Random;

@Data
public class UserAvatarUtil {

    public static String createDefaultRoboticAvatar(String username){
        int setNum = new Random().nextInt(4);
        String randomString = RandomStringUtils.randomAlphabetic(5);

        return Constants.BASE_ROBOTIC_AVA_URL + username + randomString + Constants.AVATAR_CONFIG + setNum;
    }
}
