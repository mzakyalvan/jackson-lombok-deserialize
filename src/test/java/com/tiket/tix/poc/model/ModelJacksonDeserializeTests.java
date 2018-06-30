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
