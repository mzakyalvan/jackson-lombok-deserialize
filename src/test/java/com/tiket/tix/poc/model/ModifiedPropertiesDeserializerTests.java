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
