package Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Like {
    @JsonProperty("liker")
    private String liker;

    @JsonProperty("likedPost")
    private String likedPost;

    public Like () {

    }

    public Like(String liker, String likedPost) {
        this.liker = liker;
        this.likedPost = likedPost;
    }

    public String getLiker() {
        return liker;
    }

    public void setLiker(String liker) {
        this.liker = liker;
    }

    public String getLikedPost() {
        return likedPost;
    }

    public void setLikedPost(String likedPost) {
        this.likedPost = likedPost;
    }

    @Override
    public String toString() {
        return "Like{" +
                "liker='" + liker + '\'' +
                ", liked='" + likedPost + '\'' +
                '}';
    }
}
