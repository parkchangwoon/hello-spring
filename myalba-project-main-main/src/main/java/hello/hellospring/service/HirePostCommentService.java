package hello.hellospring.service;

import hello.hellospring.domain.EmploymentType;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.HirePost;
import hello.hellospring.domain.HirePostComment;
import hello.hellospring.repository.HirePostCommentRepository;
import hello.hellospring.repository.HirePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HirePostCommentService {

    private final HirePostCommentRepository commentRepository;
    private final HirePostRepository postRepository;

    @Autowired
    public HirePostCommentService(HirePostCommentRepository commentRepository, HirePostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    //채용공고 게시글 댓글 작성 서비스
    public HirePostComment createComment(Long postId, Long authorId, String content) {
        HirePost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        HirePostComment comment = new HirePostComment();
        comment.setPost(post);
        comment.setAuthor(new Member());
        comment.getAuthor().setId(authorId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<HirePostComment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }//게시글 ID를 통해 댓글 찾는 서비스

    //댓글 삭제 서비스
    public void deleteComment(Long commentId, Long authorId, EmploymentType employmentType) {
        HirePostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        if (!comment.getAuthor().getId().equals(authorId)|| employmentType == EmploymentType.MASTER) {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다.");
        }

        commentRepository.delete(comment);
    }
}
