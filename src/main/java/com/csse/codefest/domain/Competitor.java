package com.csse.codefest.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Competitor.
 */
@Entity
@Table(name = "competitor")
@Document(indexName = "competitor")
public class Competitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Column(name = "university", nullable = false)
    private String university;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private Integer phone;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "school")
    private String school;

    @ManyToOne(optional = false)
    @NotNull
    private Event events;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Competitor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public Competitor age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUniversity() {
        return university;
    }

    public Competitor university(String university) {
        this.university = university;
        return this;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getEmail() {
        return email;
    }

    public Competitor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public Competitor phone(Integer phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public Competitor category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSchool() {
        return school;
    }

    public Competitor school(String school) {
        this.school = school;
        return this;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Event getEvents() {
        return events;
    }

    public Competitor events(Event event) {
        this.events = event;
        return this;
    }

    public void setEvents(Event event) {
        this.events = event;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Competitor competitor = (Competitor) o;
        if (competitor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), competitor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Competitor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age='" + getAge() + "'" +
            ", university='" + getUniversity() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", category='" + getCategory() + "'" +
            ", school='" + getSchool() + "'" +
            "}";
    }
}
