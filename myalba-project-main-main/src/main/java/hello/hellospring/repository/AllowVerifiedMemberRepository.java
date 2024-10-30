package hello.hellospring.repository;

import hello.hellospring.domain.AllowVerifiedMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowVerifiedMemberRepository extends JpaRepository<AllowVerifiedMember, Long> {
    boolean existsByMemberIdAndBrandId(Long memberId, Long brandId);
    List<AllowVerifiedMember> findByMemberId(Long memberId);

}
