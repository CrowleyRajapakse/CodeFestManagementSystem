package com.csse.codefest.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Resultevent.
 */
@Entity
@Table(name = "resultevent")
@Document(indexName = "resultevent")
public class Resultevent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "winner", nullable = false)
    private String winner;

    @NotNull
    @Column(name = "runner_up_1", nullable = false)
    private String runner_up1;

    @NotNull
    @Column(name = "runner_up_2", nullable = false)
    private String runner_up2;

    @Column(name = "merit_1")
    private String merit1;

    @Column(name = "merit_2")
    private String merit2;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    private Event eventresult;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWinner() {
        return winner;
    }

    public Resultevent winner(String winner) {
        this.winner = winner;
        return this;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getRunner_up1() {
        return runner_up1;
    }

    public Resultevent runner_up1(String runner_up1) {
        this.runner_up1 = runner_up1;
        return this;
    }

    public void setRunner_up1(String runner_up1) {
        this.runner_up1 = runner_up1;
    }

    public String getRunner_up2() {
        return runner_up2;
    }

    public Resultevent runner_up2(String runner_up2) {
        this.runner_up2 = runner_up2;
        return this;
    }

    public void setRunner_up2(String runner_up2) {
        this.runner_up2 = runner_up2;
    }

    public String getMerit1() {
        return merit1;
    }

    public Resultevent merit1(String merit1) {
        this.merit1 = merit1;
        return this;
    }

    public void setMerit1(String merit1) {
        this.merit1 = merit1;
    }

    public String getMerit2() {
        return merit2;
    }

    public Resultevent merit2(String merit2) {
        this.merit2 = merit2;
        return this;
    }

    public void setMerit2(String merit2) {
        this.merit2 = merit2;
    }

    public String getDescription() {
        return description;
    }

    public Resultevent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event getEventresult() {
        return eventresult;
    }

    public Resultevent eventresult(Event event) {
        this.eventresult = event;
        return this;
    }

    public void setEventresult(Event event) {
        this.eventresult = event;
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
        Resultevent resultevent = (Resultevent) o;
        if (resultevent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resultevent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Resultevent{" +
            "id=" + getId() +
            ", winner='" + getWinner() + "'" +
            ", runner_up1='" + getRunner_up1() + "'" +
            ", runner_up2='" + getRunner_up2() + "'" +
            ", merit1='" + getMerit1() + "'" +
            ", merit2='" + getMerit2() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
