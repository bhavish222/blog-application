package io.mountblue.BlogApplication.repository;

import io.mountblue.BlogApplication.entity.Post;
import io.mountblue.BlogApplication.entity.PostTag;
import io.mountblue.BlogApplication.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @Query("SELECT DISTINCT pt.post FROM PostTag pt WHERE pt.tag.id IN :tagIds")
    List<Post> findPostIdsByTagIds(
            @Param("tagIds") List<Long> tagIds
    );
}
