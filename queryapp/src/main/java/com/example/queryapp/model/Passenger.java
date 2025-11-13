package com.example.queryapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    private Integer passengerId;

    private Integer survived;
    private Integer pclass;
    private String name;
    private String sex;
    private Double age;
    private Integer sibSp;
    private Integer parch;
    private String ticket;
    private Double fare;
    private String cabin;
    private String embarked;

    public Passenger() {}

    public Integer getPassengerId() {
        return passengerId;
    }

    public Integer getSurvived() {
        return survived;
    }

    public Integer getPclass() {
        return pclass;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public Double getAge() {
        return age;
    }

    public Integer getSibSp() {
        return sibSp;
    }

    public Integer getParch() {
        return parch;
    }

    public String getTicket() {
        return ticket;
    }

    public Double getFare() {
        return fare;
    }

    public String getCabin() {
        return cabin;
    }

    public String getEmbarked() {
        return embarked;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public void setSurvived(Integer survived) {
        this.survived = survived;
    }

    public void setPclass(Integer pclass) {
        this.pclass = pclass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public void setSibSp(Integer sibSp) {
        this.sibSp = sibSp;
    }

    public void setParch(Integer parch) {
        this.parch = parch;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public void setEmbarked(String embarked) {
        this.embarked = embarked;
    }
}
