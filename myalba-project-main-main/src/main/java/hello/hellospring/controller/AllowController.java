package hello.hellospring.controller;

import hello.hellospring.domain.Allow;
import hello.hellospring.domain.AllowVerifiedMember;
import hello.hellospring.domain.Brand;
import hello.hellospring.domain.Member;
import hello.hellospring.repository.AllowVerifiedMemberRepository;
import hello.hellospring.service.AllowService;
import hello.hellospring.service.BrandService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/allow")
public class AllowController {

    private final AllowService allowService;
    private final BrandService brandService;

    @Autowired
    private AllowVerifiedMemberRepository allowVerifiedMemberRepository;

    @Autowired
    public AllowController(AllowService allowService,BrandService brandService) {
        this.allowService = allowService;
        this.brandService=brandService;
    }

    @PostMapping("/new/{brandName}")//근로계약서 인증 글 작성 메서드
    public ResponseEntity<String> createAllow(@PathVariable("brandName") String brandName,
                                              @RequestParam("title") String title,
                                              @RequestParam("content") String content,
                                              @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                              HttpSession session) throws IOException {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Brand brand = brandService.findBrandByName(brandName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand name: " + brandName));

        Allow allow = new Allow();
        allow.setTitle(title);
        allow.setContent(content);
        allow.setMember(loggedInMember);
        allow.setBrand(brand); // 브랜드 설정

        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = allowService.saveImages(images);
            allow.setImageUrls(imageUrls);
        } else {
            allow.setImageUrls(Collections.emptyList());
        }

        allowService.createAllow(allow);

        return ResponseEntity.status(HttpStatus.CREATED).body("게시글이 성공적으로 작성되었습니다.");
    }
    @GetMapping("/{brandName}/{id}")//근로계약서 인증 글 상세보기
    public ResponseEntity<?> getAllowById(@PathVariable("brandName") String brandName,@PathVariable("id") Long id, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        Optional<Allow> allow = allowService.getAllowById(id);
        return allow.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")//근로계약서 인증 글 삭제 메서드
    public ResponseEntity<?> deleteAllow(@PathVariable("id")  Long id, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        allowService.deleteAllow(id);
        return ResponseEntity.ok("근로계약서 인증 게시글이 삭제되었습니다.");
    }

    @PostMapping("/{brandName}/{id}/approve")//근로계약서 인증 허가 메서드
    public ResponseEntity<?> approveAllow(@PathVariable("brandName") String brandName, @PathVariable("id") Long id, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Brand brand = brandService.findBrandByName(brandName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand name: " + brandName));

        System.out.println("Brand ID: " + brand.getId());
        System.out.println("Brand Name: " + brand.getName());

        try {
            allowService.approveAllow(id, brand);
            return ResponseEntity.ok("인증이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/{brandName}/{id}/reject")//근로계약서 인증 거부 메서드
    public ResponseEntity<?> rejectAllow(@PathVariable("brandName") String brandName, @PathVariable("id") Long id, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            allowService.rejectAllow(id);
            return ResponseEntity.ok("근로계약서 인증 요청이 거부되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping // 근로계약서 인증 글 목록 확인 메서드
    public ResponseEntity<?> getAllAllows(HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        List<Allow> allows = allowService.getAllAllows();
        return ResponseEntity.ok(allows);
    }
}

