package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.Post;
import org.skillbox.devtales.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {

    @Query(value = "select pc " +
            "from PostComment pc " +
            "where pc.post.Id = :postId")
    List<PostComment> findPostCommentsByPostId(int postId);
}
