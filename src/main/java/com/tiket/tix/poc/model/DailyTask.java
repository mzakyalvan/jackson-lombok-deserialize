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
