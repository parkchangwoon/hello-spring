package hello.hellospring.repository;

import hello.hellospring.domain.HirePostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HirePostCommentRepository extends JpaRepository<HirePostComment, Long> {
    List<HirePostComment> findByPostId(Long postId);//게시글 ID를 통해 찾는 메서드
}
