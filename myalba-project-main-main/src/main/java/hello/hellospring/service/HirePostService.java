package hello.hellospring.service;

import hello.hellospring.domain.EmploymentType;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.HirePost;
import hello.hellospring.repository.HirePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HirePostService {

    private final HirePostRepository hirePostRepository;

    @Autowired
    public HirePostService(HirePostRepository hirePostRepository) {
        this.hirePostRepository = hirePostRepository;
    }

    //채용공고 게시글 작성 서비스
    public HirePost createPost(String title, String content, double salary, Member author) {
        HirePost post = new HirePost();
        post.setTitle(title);
        post.setContent(content);
        post.setSalary(salary);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        return hirePostRepository.save(post);
    }

    public List<HirePost> getAllPosts() {
        return hirePostRepository.findAll();
    }//모든 채용공고 게시글 불러오기 서비스

    //게시글 ID를 통해 불러오는 서비스
    public Optional<HirePost> getPostById(Long postId) {
        return hirePostRepository.findById(postId);
    }

    //채용공고 게시글 삭제 서비스
    public void deletePost(Long postId, Long authorId,EmploymentType employmentType) {
        HirePost post = getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        if (!post.getAuthor().getId().equals(authorId)|| employmentType == EmploymentType.MASTER) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }

        hirePostRepository.delete(post);
    }

    //게시글 조회수 증가 서비스
    public void increaseViewCount(Long postId) {
        hirePostRepository.findById(postId).ifPresent(post -> {
            post.setViewCount(post.getViewCount() + 1);
            hirePostRepository.save(post);
        });
    }

}
