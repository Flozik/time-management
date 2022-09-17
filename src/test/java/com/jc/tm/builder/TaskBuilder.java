package com.jc.tm.builder;

import com.jc.tm.enums.Status;
import com.jc.tm.db.entity.Comment;
import com.jc.tm.db.entity.Task;
import com.jc.tm.enums.Priority;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskBuilder {
    TaskBuilder setId(Long id);
    TaskBuilder setName(String name);
    TaskBuilder setDescription(String description);
    TaskBuilder setCreated(LocalDateTime created);
    TaskBuilder setComments(List<Comment> comments);
    TaskBuilder setStatus(Status status);
    TaskBuilder setDueDate(LocalDateTime dueDate);
    TaskBuilder setPriority(Priority priority);
    Task build();


}
