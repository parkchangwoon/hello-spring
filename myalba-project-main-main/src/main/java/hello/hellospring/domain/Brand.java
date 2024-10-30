package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Post> posts;
    @JsonIgnore
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Review> reviews;

    public List<Allow> getAllows() {
        return allows;
    }

    public void setAllows(List<Allow> allows) {
        this.allows = allows;
    }

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("brand")
    private List<Allow> allows;

    private double averageRating; // 평균 평점 필드 추가

    // Getter, Setter 추가
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}