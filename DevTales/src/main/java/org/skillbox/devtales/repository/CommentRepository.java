package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.PostComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<PostComment, Integer> {

}
