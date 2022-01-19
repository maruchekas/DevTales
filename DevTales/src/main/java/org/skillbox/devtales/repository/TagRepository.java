package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("select t from Tag t ")
    Set<Tag> findAllTags();

    @Query("select t from Tag t where t.name = :name")
    Tag findByName(String name);

}
