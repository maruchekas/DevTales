package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    String HEAD_QUERY = "select p " +
            "from Post p " +
            "where p.isActive = 1 and p.moderationStatus = 'ACCEPTED' " +
            "and p.dateTime <= current_time ";

    @Query(HEAD_QUERY + " and p.id = :id")
    Optional<Post> findPostById(int id);

    @Query("select p " +
            "from Post p " +
            "where p.id = :id")
    Optional<Post> findAnyPostById(int id);

    @Query(HEAD_QUERY)
    Page<Post> findAllActiveAndAcceptedPosts(Pageable pageable);

    @Query(HEAD_QUERY +
            "order by p.dateTime desc ")
    Page<Post> findRecentPostsSortedByDate(Pageable pageable);

    @Query(HEAD_QUERY +
            "order by p.dateTime asc ")
    Page<Post> findEarlyPostsSortedByDate(Pageable pageable);

    @Query(value = "select * " +
            "from posts p " +
            "where p.is_active = 1 " +
            "and p.moderation_status = 'ACCEPTED' " +
            "and p.`date_time` <= current_time " +
            "order by " +
            "(select sum(value) from post_votes c " +
            "where c.post_id = p.id and c.value = 1) desc", nativeQuery = true)
    Page<Post> findBestPostsSortedByLikeCount(Pageable pageable);

    @Query(HEAD_QUERY +
            "group by p order by size(p.comments) desc")
    Page<Post> findPopularPostsSortedByCommentsCount(Pageable pageable);

    @Query(HEAD_QUERY +
            "and (p.title like '%'||:text||'%' or p.text like '%'||:text||'%') " +
            "group by p " +
            "order by p.dateTime desc ")
    Page<Post> findPostsByText(String text, Pageable pageable);

    @Query(value = "select * " +
            "from posts p " +
            "join tag2post tp on p.id = tp.post_id " +
            "join tags t on tp.tag_id = t.id " +
            "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and p.date_time <= current_time " +
            "and t.name = :tag " +
            "group by p.id ", nativeQuery = true)
    Page<Post> findPostsByTag(String tag, Pageable pageable);

    @Query(value = "select * " +
            "from posts p " +
            "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' " +
            "and p.date_time <= current_time " +
            "and date_format(p.date_time, '%Y-%m-%d') = :date ", nativeQuery = true)
    Page<Post> findPostsByDate(String date, Pageable pageable);

    @Query(value = "select * " +
            "from posts p " +
            "where p.is_active = 1 " +
            "and p.moderation_status = :status " +
            "group by p.id " +
            "order by p.date_time ", nativeQuery = true)
    Page<Post> findNewPostsForModeration(String status, Pageable pageable);

    @Query(value = "select count(p) from Post p " +
            "where p.isActive = 1 and p.moderationStatus = 'NEW' ")
    int findCountPostsForModeration();

    @Query(value = "select * from posts p " +
            "where p.user_id = :userId and p.moderation_status = :status and p.is_active = :isActive ",
            nativeQuery = true)
    Page<Post> findMyPosts(String status, int userId, byte isActive, Pageable pageable);

    @Query(value = "select date_format(p.date_time, '%Y') as p_year " +
            "from posts p " +
            "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and p.date_time <= current_time " +
            "group by p_year " +
            "order by p_year ", nativeQuery = true)
    Integer[] findYearsOfPosts();

    @Query(value = "select date_format(p.date_time, '%Y-%m-%d') as p_date, count(p.id) as p_count " +
            "from posts p " +
            "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and p.date_time <= current_time " +
            "and date_format(p.date_time, '%Y') = :year " +
            "group by p_date ", nativeQuery = true)
    List<String> findPostsByYear(int year);

    @Query("select count(p) from Post p where p.user.id = :id")
    Optional<Integer> findCountAllPostsByUserId(int id);

    @Query("select sum(p.viewCount) from Post p ")
    int findViewsCount();

    @Query("select sum(p.viewCount) from Post p where p.user.id = :id")
    Optional<Integer> findViewsCountByUserId(int id);

    @Query("select min(p.dateTime) from Post p ")
    Optional<LocalDateTime> findFirstPost();

    @Query("select min(p.dateTime) from Post p where p.user.id = :id")
    Optional<LocalDateTime> findFirstPostByUserId(int id);

}
