package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GlobalSettingRepository extends JpaRepository<GlobalSetting, Integer> {

    Optional<GlobalSetting> findByCode(String code);

}
