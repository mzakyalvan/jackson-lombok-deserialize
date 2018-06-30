# README

## Introduction

This project is poc for json deserialization using builder object generated using [lombok's](https://projectlombok.org/contributing/lombok-execution-path) ```@Builder``` annotation.

Motivation is to create immutable object as input for concurrent programming.

As alternatives, you can use setter method with package access modifier, or in Spring Mvc land, instruct data binder of your request to use direct field access binding.

## Prepare Project

Pom content

```xml

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tiket.tix.poc</groupId>
    <artifactId>jackson-lombok-deserialize</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>jackson-lombok-deserialize</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.14.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <jackson.version>2.9.6</jackson.version>
        <lombok.version>1.18.0</lombok.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.module</groupId>
            <artifactId>jackson-module-parameter-names</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jdk8</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>


```

> Please note, ```spring-boot-starter-webmvc``` is not used directly, added for transitively import other required dependencies.


## First Sample

Deserialize sample using following types.

```java

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


```

```java

package com.tiket.tix.poc.model;

/**
 * @author zakyalvan
 */
public enum Gender {
    MALE, FEMALE
}


```

```java

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


```

Sample json used in test (```com.tiket.tix.poc.model.ModelJacksonDeserializeTests``) is following

```json

{
  "firstName": "Zaky", 
  "lastName": "Alvan", 
  "dateOfBirth": "18-06-1985", 
  "gender": "MALE", 
  "contact": {
    "phoneNumber": "081320144088", 
    "emailAddress": "zaky.alvan@tiket.com"
  }
}

```

Sample test case

```java

package com.tiket.tix.poc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiket.tix.poc.SampleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(classes = SampleApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class ModelJacksonDeserializeTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testBuilderDeserialize() throws IOException {
        Resource jsonResource = new ClassPathResource("person.json");
        Person person = objectMapper.readValue(jsonResource.getInputStream(), Person.class);
        assertThat(person.getFirstName(), equalTo("Zaky"));
        assertThat(person.getLastName(), equalTo("Alvan"));
        assertThat(person.getDateOfBirth(), equalTo(LocalDate.of(1985, Month.JUNE, 18)));
        assertThat(person.getGender(), equalTo(Gender.MALE));
        assertThat(person.getContact().getEmailAddress(), equalTo("zaky.alvan@tiket.com"));
        assertThat(person.getContact().getPhoneNumber(), equalTo("081320144088"));
    }
}


```


## Second Sample

Non linear mapping (pojo property name different with json field name) also provided using following type

```java

package com.tiket.tix.poc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * @author zakyalvan
 */
@Value
@JsonDeserialize(builder = DailyTask.DailyTaskBuilder.class)
public class DailyTask {
    private String name;
    private String description;
    private LocalDate scheduledDate;
    private boolean completed;

    @Builder
    protected DailyTask(String taskName, String taskDescription, LocalDate scheduledDate, boolean completedTask) {
        this.name = taskName;
        this.description = taskDescription;
        this.scheduledDate = scheduledDate;
        this.completed = completedTask;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class DailyTaskBuilder {
        private LocalDate scheduledDate;

        @JsonFormat(pattern = "dd MMM yyyy")
        public DailyTaskBuilder scheduledDate() {
            this.scheduledDate = scheduledDate;
            return this;
        }
    }
}


```

Sample json for task

```json

{
  "taskName": "Room Cleanup",
  "taskDescription": "Clean the room using new carbol",
  "scheduledDate": "30 Jun 2018", 
  "completedTask": false
}

```

Simple test case

```java

package com.tiket.tix.poc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiket.tix.poc.SampleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author zakyalvan
 */
@SpringBootTest(classes = SampleApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class ModifiedPropertiesDeserializerTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testBuilderDeserialize() throws IOException {
        Resource jsonResource = new ClassPathResource("task.json");
        DailyTask task = objectMapper.readValue(jsonResource.getInputStream(), DailyTask.class);
        assertThat(task.getName(), equalTo("Room Cleanup"));
        assertThat(task.getDescription(), equalTo("Clean the room using new carbol"));
        assertThat(task.getScheduledDate(), equalTo(LocalDate.of(2018, Month.JUNE, 30)));
        assertThat(task.isCompleted(), is(false));
    }
}


```

Browse this project for details.

## Inspired By

- https://blog.d46.us/java-immutable-lombok/
- https://groups.google.com/forum/#!topic/project-lombok/PGyFNUP-Ofs