package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //회원 가입 서비스
    public Long join(Member member){
            vaildateDuplicateMember(member);
            memberRepository.save(member);
            return member.getId();
    }

    //회원 중복 가입 방지 서비스
    private void vaildateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }
    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }//회원 ID를 통해 찾는 서비스

    //로그인 서비스
    public Optional<Member> login(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            Member foundMember = member.get();
            //Base64를 통해 비밀번호 암호화한 부분을 디코딩하여 비교
            String decodedPassword = new String(Base64.getDecoder().decode(foundMember.getPassword1()));
            if (decodedPassword.equals(password)) {
                return member;
            }
        }
        return Optional.empty();
    }
    public boolean isEmailAlreadyRegistered(String email) {
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        return existingMember.isPresent();
    }//이메일 중복 확인
    public boolean isNicknameAlreadyRegistered(String nickname) {
        Optional<Member> existingMember = memberRepository.findByNickname(nickname);
        return existingMember.isPresent();
    }//닉네임 중복 확인
    public boolean isNicknameAlreadyUsed(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }//닉네임 중복 확인
    public void updateMember(Member member) {
        memberRepository.save(member);
    }//회원 정보 최신화

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }//ID를 통해 회원 찾는 서비스

}
