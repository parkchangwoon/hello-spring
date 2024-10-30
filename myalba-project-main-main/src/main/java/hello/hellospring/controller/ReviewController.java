package hello.hellospring.controller;

import hello.hellospring.domain.EmploymentType;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.Review;
import hello.hellospring.service.BrandService;
import hello.hellospring.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands/{name}/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final BrandService brandService;

    public ReviewController(ReviewService reviewService, BrandService brandService) {
        this.reviewService = reviewService;
        this.brandService = brandService;
    }

    @PostMapping//브랜드 한줄 평가 및 평점 부여 메서드
    public ResponseEntity<String> createReview(@PathVariable("name") String name, @RequestBody ReviewForm form, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인시 사용 가능합니다.");
        }
        //아르바이트 회원에게만 작성 권한 부여
        if (loggedInMember.getEmploymentType() != EmploymentType.EMPLOYEE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("아르바이트생 회원만 작성 가능합니다.");
        }
        Long brandId = brandService.findBrandIdByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand name"));

        try {
            Review review = reviewService.saveReview(brandId, loggedInMember.getId(), form.getContent(), form.getRating());
            return ResponseEntity.ok("리뷰가 성공적으로 작성되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping//해당 브랜드의 한줄 평가 목록 반환 메서드
    public ResponseEntity<List<Review>> listReviewsByBrand(@PathVariable("name") String name) {
        Long brandId = brandService.findBrandIdByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand name"));

        List<Review> reviews = reviewService.findReviewsByBrandId(brandId);
        return ResponseEntity.ok(reviews);
    }
}