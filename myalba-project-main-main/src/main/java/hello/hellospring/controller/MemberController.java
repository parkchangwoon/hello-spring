package hello.hellospring.controller;
import hello.hellospring.domain.*;
import hello.hellospring.repository.AllowVerifiedMemberRepository;
import hello.hellospring.service.MemberService;
import hello.hellospring.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final PostService postService;
    private final AllowVerifiedMemberRepository allowVerifiedMemberRepository;

    @Autowired
    public MemberController(MemberService memberService,PostService postService,AllowVerifiedMemberRepository allowVerifiedMemberRepository) {
        this.memberService = memberService;
        this.postService=postService;
        this.allowVerifiedMemberRepository=allowVerifiedMemberRepository;
    }

    @GetMapping("/members/new")
    public ResponseEntity<String> createForm() {
        // 회원 가입 폼을 반환합니다.
        return ResponseEntity.ok("회원 가입 폼을 보여줍니다.");
    }

    @PostMapping("/members/new/employee") // 아르바이트생 회원 가입 메서드
    public ResponseEntity<String> createemployee(@RequestBody MemberForm form) {
        ResponseEntity<String> BAD_REQUEST = joinvalidation(form);
        if (BAD_REQUEST != null) return BAD_REQUEST;
        // 회원 정보 생성 및 저장
        Member member = new Member();
        member.setName(form.getName());
        member.setNickname(form.getNickname());
        member.setEmail(form.getEmail());
        member.setPassword1(form.getPassword1());
        member.setPassword2(form.getPassword2());
        member.setEmploymentType(EmploymentType.EMPLOYEE);//enum값 EMPLOYEE로 지정하여 저장(아르바이트 회원 구분)
        memberService.join(member);

        return ResponseEntity.status(HttpStatus.CREATED).body("알바생 회원 가입이 완료되었습니다.");
    }

    @PostMapping("/members/new/boss") // 자영업자 회원 가입 메서드
    public ResponseEntity<String> createboss(@RequestBody MemberForm form) {
        ResponseEntity<String> BAD_REQUEST = joinvalidation(form);
        if (BAD_REQUEST != null) return BAD_REQUEST;
        //회원 정보 생성 및 저장
        Member member = new Member();
        member.setName(form.getName());
        member.setNickname(form.getNickname());
        member.setEmail(form.getEmail());
        member.setPassword1(form.getPassword1());
        member.setPassword2(form.getPassword2());
        member.setEmploymentType(EmploymentType.BOSS);//enum값 BOSS로 지정하여 저장(자영업자 회원 구분)
        memberService.join(member);

        return ResponseEntity.status(HttpStatus.CREATED).body("자영업자 회원 가입이 완료되었습니다.");
    }

    private ResponseEntity<String> joinvalidation(MemberForm form) {
        // 이메일 중복 확인
        if (memberService.isEmailAlreadyRegistered(form.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 등록된 이메일입니다. 다른 이메일을 사용해주세요.");
        }
        // 닉네임 중복 확인
        if (memberService.isNicknameAlreadyRegistered(form.getNickname())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 등록된 닉네임입니다. 다른 닉네임을 사용해주세요.");
        }
        // 비밀번호가 비어 있는지 확인
        if (form.getPassword1() == null || form.getPassword1().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 입력해주세요.");
        }
        // 비밀번호가 비어 있는지 확인
        if (form.getPassword2() == null || form.getPassword2().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 다시 입력해주세요.");
        }
        // 비밀번호 일치 여부 확인
        if (!form.getPassword1().equals(form.getPassword2())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 다시 확인해주세요.");
        }
        // 필수 입력 항목 검증
        if (!form.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일, 이름, 닉네임을 모두 입력해주세요.");
        }
        return null;
    }

    @PostMapping("/login")//로그인 메서드
    public ResponseEntity<?> login(@RequestBody MemberForm form, HttpSession session) {
        System.out.println("로그인 시도: " + form.getEmail() + ", " + form.getPassword1());
        Optional<Member> loginResult = memberService.login(form.getEmail(), form.getPassword1());
        if (loginResult.isPresent()) {
            session.setAttribute("loggedInMember", loginResult.get());
            return ResponseEntity.ok(loginResult.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
    }



    @GetMapping("/logout")//로그아웃 메서드
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃이 성공적으로 완료되었습니다.");
    }

    @GetMapping("/profile") // 프로필 정보 반환 메서드
    public ResponseEntity<Map<String, Object>> showProfile(HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Post> userPosts = postService.findPostsByAuthorId(loggedInMember.getId());
        List<AllowVerifiedMember> verifiedMembers = allowVerifiedMemberRepository.findByMemberId(loggedInMember.getId());

        List<String> brandNames = verifiedMembers.stream()
                .map(vm -> vm.getBrand().getName())
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("member", loggedInMember);
        response.put("posts", userPosts);
        response.put("brands", brandNames);//인증된 브랜드 목록 반환

        return ResponseEntity.ok(response);
    }


    @PostMapping("/profile/updateNickname")//회원 닉네임 수정 메서드
    public ResponseEntity<String> updateNickname(@RequestBody Map<String, String> request, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute("loggedInMember");
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String newNickname = request.get("newNickname");
        if (newNickname == null || newNickname.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새 닉네임을 입력해주세요.");
        }

        if (memberService.isNicknameAlreadyUsed(newNickname)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 닉네임입니다.");
        }

        loggedInMember.setNickname(newNickname);
        memberService.updateMember(loggedInMember);
        session.setAttribute("loggedInMember", loggedInMember); // 세션 정보 업데이트하여 새로운 닉네임 표기
        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }
    @GetMapping("/members/{memberId}/profile") // 다른 사용자 프로필 정보 반환 메서드
    public ResponseEntity<Map<String, Object>> viewProfile(@PathVariable("memberId") Long memberId) {
        Optional<Member> memberOpt = memberService.findById(memberId);
        if (memberOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Member member = memberOpt.get();
        List<Post> userPosts = postService.findPostsByAuthorId(memberId);

        // 인증된 브랜드 목록 조회
        List<AllowVerifiedMember> verifiedMembers = allowVerifiedMemberRepository.findByMemberId(memberId);
        List<Brand> verifiedBrands = verifiedMembers.stream()
                .map(AllowVerifiedMember::getBrand)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("member", member);
        response.put("posts", userPosts);
        response.put("verifiedBrands", verifiedBrands);

        return ResponseEntity.ok(response);
    }



}
