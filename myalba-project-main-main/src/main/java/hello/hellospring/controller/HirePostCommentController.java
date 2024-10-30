package hello.hellospring.controller;

import hello.hellospring.domain.EmploymentType;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.HirePostComment;
import hello.hellospring.service.HirePostCommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hire")
public class HirePostCommentController {

    private final HirePostCommentService commentService;

    public HirePostCommentController(HirePostCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}/comment")//채용 공고 게시판 댓글 작성 메서드
    public ResponseEntity<String> createComment(@PathVariable("id") Long postId, @RequestBody HirePostCommentForm form, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        //댓글을 달 수 있는 권한은 아르바이트생만 부여
        if (loggedInMember.getEmploymentType() != EmploymentType.EMPLOYEE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("알바생만 댓글을 작성할 수 있습니다.");
        }

        try {
            commentService.createComment(postId, loggedInMember.getId(), form.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 성공적으로 작성되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/comments")//채용 공고 게시판 댓글 불러오기 메서드
    public ResponseEntity<List<HirePostComment>> getCommentsByPostId(@PathVariable("id") Long postId) {
        List<HirePostComment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comments/{commentId}")//댓글 삭제 메서드
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            commentService.deleteComment(commentId, loggedInMember.getId(), loggedInMember.getEmploymentType());
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
