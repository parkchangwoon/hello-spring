package hello.hellospring.repository;

import hello.hellospring.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);//브랜드 이름을 통해 브랜드 찾는 메서드
    Optional<Brand> findBrandById(Long brandId);
    Optional<Brand> findById(Long id);//브랜드 아이디를 통해 브랜드 찾는 메서드
    @Query("SELECT b FROM Brand b ORDER BY b.averageRating DESC")
    List<Brand> findAllOrderByAverageRatingDesc();//브랜드 평점 평균 높은 순서로 불러오는 메서드
}