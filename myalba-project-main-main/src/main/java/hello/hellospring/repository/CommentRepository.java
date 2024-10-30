package hello.hellospring.repository;

import hello.hellospring.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);//게시글 ID를 통해 찾는 메서드
    List<Comment> findByParentId(Long parentId); //부모 댓글 ID로 대댓글 찾는 메서드
}