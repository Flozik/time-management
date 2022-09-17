package com.jc.tm.dto;

import com.jc.tm.enums.Importance;
import com.jc.tm.enums.Status;
import com.jc.tm.enums.Priority;
import com.jc.tm.enums.Urgency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private Importance importance;
  private Urgency urgency;
}
