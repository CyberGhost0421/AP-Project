package com.linkedin.clientapp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
    @JsonProperty("commenter")
    private String commenter;

    @JsonProperty("commentPost")
    private String commentPost;

    public Comment(){

    }
    public Comment(String commenter, String commentPost) {
        this.commenter = commenter;
        this.commentPost = commentPost;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getCommentPost() {
        return commentPost;
    }

    public void setCommentPost(String commentPost) {
        this.commentPost = commentPost;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commenter='" + commenter + '\'' +
                ", commentPost='" + commentPost + '\'' +
                '}';
    }
}
