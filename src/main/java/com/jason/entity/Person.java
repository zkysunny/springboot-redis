package com.jason.entity;

import java.io.Serializable;

/**
 * Created by jason on 2017/12/18.
 */
public class Person implements Serializable{

    private Integer id;
    private String name;
    private String school;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", school='" + school + '\'' +
                '}';
    }

    public Person(Integer id, String name, String school) {
        this.id = id;
        this.name = name;
        this.school = school;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Person() {
    }
}
