package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.GlobalSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {

}
