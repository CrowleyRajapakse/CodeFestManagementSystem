package com.csse.codefest.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Feedback.
 */
@Entity
@Table(name = "feedback")
@Document(indexName = "feedback")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "recipient", nullable = false)
    private String recipient;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public Feedback recipient(String recipient) {
        this.recipient = recipient;
        return this;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getEmail() {
        return email;
    }

    public Feedback email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public Feedback message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
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
        Feedback feedback = (Feedback) o;
        if (feedback.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feedback.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + getId() +
            ", recipient='" + getRecipient() + "'" +
            ", email='" + getEmail() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
