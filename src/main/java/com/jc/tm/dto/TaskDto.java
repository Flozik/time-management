package com.jc.tm.dto;

import com.jc.tm.util.Status;
import com.jc.tm.util.Priority;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Data
public class TaskDto {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    private String created;
    private Collection<CommentDto> comments;
    private Status status;
    private String dueDate;
    private Priority priority = Priority.NORMAL;
    private int progress;
    private ProjectDto projectName;
}
