package hello.hellospring.controller;


import hello.hellospring.domain.EmploymentType;
import org.springframework.web.multipart.MultipartFile;

import java.beans.ConstructorProperties;
import java.util.List;

public class PostForm {
    private String title;
    private String content;
    private String brandName; // 브랜드 이름을 담을 변수
    private Long brandId; // 브랜드 아이디를 담을 변수
    private int viewCount; // 조회수 추가

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    private List<MultipartFile> images;

    // 생성자, 게터, 세터
    @ConstructorProperties({"title", "content", "brandName", "brandId", "viewCount"})
    public PostForm(String title, String content, String brandName, Long brandId, int viewCount) {
        this.title = title;
        this.content = content;
        this.brandName = brandName;
        this.brandId = brandId;
        this.viewCount = viewCount;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}

