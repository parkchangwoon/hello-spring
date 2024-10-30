package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);//ID를 통해 찾는 메서드
    Optional<Member> findByName(String name);//이름을 통해 찾는 메서드
    Optional<Member> findByEmail(String email);//이메일을 통해 찾는 메서드
    Optional<Member> findByNickname(String nickname);//닉네임을 통해 찾는 메서드

    List<Member> findAll();//모든 회원을 불러오는 메서드

}
