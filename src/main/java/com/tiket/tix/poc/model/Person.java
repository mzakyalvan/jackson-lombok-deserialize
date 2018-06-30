package com.tiket.tix.poc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @author zakyalvan
 */
@Slf4j
@Value
@JsonDeserialize(builder = Person.Builder.class)
public class Person {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Contact contact;

    @lombok.Builder(builderClassName = "Builder")
    public Person(String firstName, String lastName, LocalDate dateOfBirth, Gender gender, Contact contact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contact = contact;
    }

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        protected LocalDate dateOfBirth;

        @JsonFormat(pattern = "dd-MM-yyyy")
        Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }
    }
}
