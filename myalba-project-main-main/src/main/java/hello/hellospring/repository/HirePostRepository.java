package hello.hellospring.repository;

import hello.hellospring.domain.HirePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HirePostRepository extends JpaRepository<HirePost, Long> {
}
