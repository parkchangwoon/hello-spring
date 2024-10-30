package hello.hellospring.controller;

import java.beans.ConstructorProperties;

public class AllowForm {

    private String title;
    private String content;
    private Long brandId;

    @ConstructorProperties({"title", "content", "brandId"})
    public AllowForm(String title, String content, Long brandId) {
        this.title = title;
        this.content = content;
        this.brandId = brandId;
    }

    // Getters and Setters
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }
}
