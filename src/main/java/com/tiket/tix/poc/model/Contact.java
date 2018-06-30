package com.tiket.tix.poc.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Email;

import java.io.Serializable;

/**
 * @author zakyalvan
 */
@Value
@Builder
@JsonDeserialize(builder = Contact.ContactBuilder.class)
public class Contact implements Serializable {
    @Email
    private String emailAddress;

    private String phoneNumber;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ContactBuilder {

    }
}
