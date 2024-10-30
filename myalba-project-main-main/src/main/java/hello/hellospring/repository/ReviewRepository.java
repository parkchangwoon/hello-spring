package hello.hellospring.repository;

import hello.hellospring.domain.Brand;
import hello.hellospring.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBrandId(Long brandId);//브랜드 ID를 통해 찾는 메서드
}
