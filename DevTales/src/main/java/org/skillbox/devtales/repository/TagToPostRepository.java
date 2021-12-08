package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.TagToPost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagToPostRepository extends CrudRepository<TagToPost, Integer> {

    @Query(value = "SELECT * FROM tag2post WHERE tag_id = :id", nativeQuery = true)
    List<TagToPost> findAllTagsById(@Param("id") int id);

}
