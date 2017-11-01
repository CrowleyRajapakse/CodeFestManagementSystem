package com.csse.codefest.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Feedback entity.
 */
public class FeedbackDTO implements Serializable {

    private Long id;

    @NotNull
    private String recipient;

    @NotNull
    private String email;

    @NotNull
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FeedbackDTO feedbackDTO = (FeedbackDTO) o;
        if(feedbackDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feedbackDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FeedbackDTO{" +
            "id=" + getId() +
            ", recipient='" + getRecipient() + "'" +
            ", email='" + getEmail() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
