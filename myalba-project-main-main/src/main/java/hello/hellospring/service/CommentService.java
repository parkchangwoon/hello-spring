package hello.hellospring.service;

import hello.hellospring.domain.Comment;
import hello.hellospring.domain.EmploymentType;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.Post;
import hello.hellospring.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MemberService memberService;

    public CommentService(CommentRepository commentRepository, PostService postService, MemberService memberService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.memberService = memberService;
    }

    //댓글 작성 서비스
    public Comment createComment(Long postId, Long authorId, String content, Long parentId) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        Member author = memberService.findOne(authorId).orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));

        // 통합게시판이 아닌 경우에만 댓글 작성자 유형 제한을 적용
        if (post.getBrand() != null) {
            EmploymentType postAuthorEmploymentType = post.getAuthor().getEmploymentType();
            EmploymentType commentAuthorEmploymentType = author.getEmploymentType();
            if ((postAuthorEmploymentType == EmploymentType.EMPLOYEE && commentAuthorEmploymentType == EmploymentType.BOSS) ||
                    (postAuthorEmploymentType == EmploymentType.BOSS && commentAuthorEmploymentType == EmploymentType.EMPLOYEE)) {
                throw new IllegalArgumentException("댓글을 달 수 없습니다. 작성자 유형에 따른 제한이 있습니다.");
            }
        }

        Comment parent = null;
        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID"));
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setParent(parent);

        return commentRepository.save(comment);
    }

    //댓글 수정 서비스
    public void updateComment(Long postId, Long commentId, Long authorId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재 하지 않습니다."));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to the post");
        }
        //삭제된 댓글 수정 방지
        if (comment.isDeleted()) {
            throw new IllegalArgumentException("삭제된 댓글은 수정할 수 없습니다.");
        }
        //작성자와 현재 로그인 한 회원이 일치해야 수정 허용
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다.");
        }

        comment.setContent(content);
        commentRepository.save(comment);
    }

    //댓글 삭제 서비스
    public void deleteComment(Long postId, Long commentId, Long authorId, EmploymentType employmentType) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Comment does not belong to the post");
        }
        //작성자와 현재 로그인 한 회원이 일치, 또는 관리자 계정일 경우 삭제 허용
        if (!comment.getAuthor().getId().equals(authorId) && employmentType != EmploymentType.MASTER) {
            throw new IllegalArgumentException("댓글 작성자가 아니거나 관리자 권한이 없습니다.");
        }

        if (comment.getReplies().isEmpty()) { // 대댓글이 없는 경우
            commentRepository.delete(comment);
        } else { // 대댓글이 있는 경우
            comment.setDeleted(true);
            comment.setContent("삭제된 댓글입니다.");
            commentRepository.save(comment);
        }
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }//게시글 ID를 통해 댓글 찾는 서비스

    public List<Comment> getReplies(Long parentId) {
        return commentRepository.findByParentId(parentId);
    }//대댓글을 찾는 메서드
    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }//ID를 통해 댓글을 찾는 서비스

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }//댓글 저장 서비스
}