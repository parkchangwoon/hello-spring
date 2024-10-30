package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.domain.Post;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public LikeService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    //좋아요 기능 서비스
    public void likePost(Long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        //좋아요를 누른 회원이면 다시 클릭시 좋아요 취소
        if (post.getLikes().contains(member)) {
            post.getLikes().remove(member);
            post.setLikeCount(post.getLikeCount() - 1);
        } else {//좋아요를 누르지 않은 회원이면 클릭시 좋아요 증가
            post.getLikes().add(member);
            post.setLikeCount(post.getLikeCount() + 1);
        }
        postRepository.save(post);
    }
}
