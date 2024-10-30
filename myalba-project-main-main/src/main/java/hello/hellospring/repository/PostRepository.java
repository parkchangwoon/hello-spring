package hello.hellospring.repository;

import hello.hellospring.domain.EmploymentType;
import hello.hellospring.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시글 저장 메서드 추가
    Post save(Post post);

    List<Post> findByBrandId(Long id);//브랜드 ID를 통해 찾는 메서드

    List<Post> findByBrandName(String brandName);//브랜드 이름을 통해 찾는 메서드
    List<Post> findByBrandNameAndEmploymentType(String brandName, EmploymentType employmentType);//브랜드 이름과 회원 유형을 통해 찾는 메서드
    List<Post> findByAuthorId(Long authorId);//작성자 ID를 통해 찾는 메서드

    List<Post> findByEmploymentTypeIsNull();//회원 유형이 NULL값을 찾는 메서드(통합 게시글)

    //인기 게시글 불러오는 메서드
    @Query("SELECT p FROM Post p WHERE p.employmentType = :employmentType ORDER BY p.likeCount DESC")
    List<Post> findPostsByEmploymentTypeOrderByLikesDesc(@Param("employmentType") EmploymentType employmentType);

}



