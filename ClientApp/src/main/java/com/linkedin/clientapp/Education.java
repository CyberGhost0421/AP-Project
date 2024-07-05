package com.linkedin.clientapp;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Education {
    @JsonProperty("id")
    private String id;

    @JsonProperty("institute")
    private String institute;
    @JsonProperty("study")
    private String study;
    @JsonProperty("startedDate")
    private Date startedDate;
    @JsonProperty("finishedDate")
    private Date finishedDate;
    @JsonProperty("grade")
    private String grade;
    @JsonProperty("activities")
    private String activities;
    @JsonProperty("description")
    private String description;

    public Education(String id ,String institute ,String study ,Date startedDate ,Date finishedDate ,String grade ,String activities ,String description ){
        this.id = id;
        this.institute = institute;
        this.study = study;
        this.startedDate = startedDate;
        this.finishedDate = finishedDate;
        this.grade = grade;
        this.activities = activities;
        this.description = description;
    }
    public Education(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Education{" +
                "id='" + id + '\'' +
                ", institute='" + institute + '\'' +
                ", study='" + study + '\'' +
                ", startedDate='" + startedDate + '\'' +
                ", finishedDate=" + finishedDate + ':' +
                ", grade='" + grade + '\'' +
                ", activities='" + activities + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
