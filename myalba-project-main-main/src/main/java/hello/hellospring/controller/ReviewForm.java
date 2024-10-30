package hello.hellospring.controller;


public class ReviewForm {
    private String content;
    private double rating;

    // Getter, Setter 추가
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
