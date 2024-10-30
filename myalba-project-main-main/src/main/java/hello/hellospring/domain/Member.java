package hello.hellospring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Base64;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String nickname;
    private String password1;
    private String password2;
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    public List<Allow> getAllows() {
        return allows;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Review> reviews;


    public void setAllows(List<Allow> allows) {
        this.allows = allows;
    }

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("member")
    private List<Allow> allows;

    public Member() {}

    public Member(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = Base64.getEncoder().encodeToString(password1.getBytes());
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = Base64.getEncoder().encodeToString(password2.getBytes());
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}