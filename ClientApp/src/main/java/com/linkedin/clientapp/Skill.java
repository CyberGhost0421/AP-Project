package com.linkedin.clientapp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Skill {

    @JsonProperty("id")
    private String id;
    @JsonProperty("skillTitle")
    private String skillTitle;
    @JsonProperty("skillDetail")
    private String skillDetail;

    public Skill(String id , String skillTitle , String skillDetail){
        this.id = id;
        this.skillTitle = skillTitle;
        this.skillDetail = skillDetail;
    }
    public Skill(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkillTitle() {
        return skillTitle;
    }

    public void setSkillTitle(String skillTitle) {
        this.skillTitle = skillTitle;
    }

    public String getSkillDetail() {
        return skillDetail;
    }

    public void setSkillDetail(String skillDetail) {
        this.skillDetail = skillDetail;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id='" + id + '\'' +
                ", skillTitle='" + skillTitle + '\'' +
                ", skillDetail='" + skillDetail + '\'' +
                '}';
    }

}
