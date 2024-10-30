package hello.hellospring.repository;

import hello.hellospring.domain.Allow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllowRepository extends JpaRepository<Allow, Long> {
}
