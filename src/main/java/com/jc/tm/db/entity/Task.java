package com.jc.tm.db.entity;

import com.jc.tm.util.Priority;
import com.jc.tm.util.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    @Enumerated(EnumType.STRING)
    //default status for any task is to do
    private Status status = Status.TODO;
    @Column(columnDefinition = "TIMESTAMP", name = "due_date")
    private LocalDateTime dueDate;
    @Enumerated(EnumType.STRING)
    //default priority for any task is normal
    private Priority priority = Priority.NORMAL;
    private int progress;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project projects;

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private Task task;

        public TaskBuilder() {
            task = new Task();
        }

        public TaskBuilder setId(long id) {
            task.id = id;
            return this;
        }

        public TaskBuilder setName(String name) {
            task.name = name;
            return this;
        }

        public TaskBuilder setDescription(String description) {
            task.description = description;
            return this;
        }

        public TaskBuilder setCreated(LocalDateTime created) {
            task.created = created;
            return this;
        }

        public TaskBuilder setComments(List<Comment> comments) {
            task.comments = comments;
            return this;
        }

        public TaskBuilder setStatus(Status status) {
            task.status = status;
            return this;
        }

        public TaskBuilder setDueDate(LocalDateTime dueDate) {
            task.dueDate = dueDate;
            return this;
        }

        public TaskBuilder setPriority(Priority priority) {
            task.priority = priority;
            return this;
        }

        public TaskBuilder setProgress(int progress) {
            task.progress = progress;
            return this;
        }

        public Task build() {
            return task;
        }
    }
}