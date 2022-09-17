package com.jc.tm.builder;

import com.jc.tm.enums.Status;
import com.jc.tm.db.entity.Comment;
import com.jc.tm.db.entity.Task;
import com.jc.tm.enums.Priority;
import java.time.LocalDateTime;
import java.util.List;

public class TaskBuilderImpl implements TaskBuilder {
    Task task = new Task();

    @Override
    public TaskBuilder setId(Long id) {
        task.setId(id);
        return this;
    }

    @Override
    public TaskBuilder setName(String name) {
        task.setName(name);
        return this;
    }

    @Override
    public TaskBuilder setDescription(String description) {
        task.setDescription(description);
        return this;
    }

    @Override
    public TaskBuilder setCreated(LocalDateTime created) {
        task.setCreated(created);
        return this;
    }

    @Override
    public TaskBuilder setComments(List<Comment> comments) {
        task.setComments(comments);
        return this;
    }

    @Override
    public TaskBuilder setStatus(Status status) {
        task.setStatus(status);
        return this;
    }

    @Override
    public TaskBuilder setDueDate(LocalDateTime dueDate) {
        task.setDueDate(dueDate);
        return this;
    }

    @Override
    public TaskBuilder setPriority(Priority priority) {
        task.setPriority(priority);
        return this;
    }

    @Override
    public Task build(){
        return task;
    }
}
