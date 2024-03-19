package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.PostTag;
import io.mountblue.BlogApplication.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @Query("SELECT DISTINCT pt.post FROM PostTag pt WHERE pt.tag IN :tagIds")
    List<Post> findPostIdsByTagIds(@Param("tagIds") List<Tag> tagIds);
}
