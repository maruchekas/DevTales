package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.Captcha;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends CrudRepository<Captcha, Integer> {

}
