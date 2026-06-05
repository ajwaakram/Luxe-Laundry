package com.find.luxelaundary.Model;

public class FeedbackModel {
    String rating;
    String review;
    String userId;

    public FeedbackModel() {
    }

    public FeedbackModel(String rating, String review, String userId) {
        this.rating = rating;
        this.review = review;
        this.userId = userId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
