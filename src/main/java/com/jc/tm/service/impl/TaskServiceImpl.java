package com.jc.tm.service.impl;

import com.jc.tm.db.dao.jpa.CommentDao;
import com.jc.tm.db.dao.jpa.TaskDao;
import com.jc.tm.db.entity.Comment;
import com.jc.tm.db.entity.Task;
import com.jc.tm.dto.PaginationDto;
import com.jc.tm.dto.TaskDto;
import com.jc.tm.service.ITaskService;
import com.jc.tm.util.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * this TaskServiceImpl class cooperate with DAO of Task and Comments
 */
@Slf4j
@Service
public class TaskServiceImpl implements ITaskService {

  private final TaskDao taskDao;
  private final CommentDao commentDao;

  @Autowired
  public TaskServiceImpl(TaskDao taskDao, CommentDao commentDao) {
    this.taskDao = taskDao;
    this.commentDao = commentDao;
  }

  @Override
  public Task createTask(Task task) {
    log.info("saveTask input values:{}", task);
    task.setCreated(LocalDateTime.now());
    task = taskDao.save(task);
    log.info("new task {} was saved", task);
    return task;
  }

  @Override
  public Task updateTaskStatus(Task freshTask, TaskDto status) {
    log.info("updateTask input values:{}", status);
    Status newStatus = status.getStatus();
    freshTask.setStatus(newStatus);
    taskDao.save(freshTask);
    return freshTask;
  }

  @Override
  public Task updateTaskNew(Task task, TaskDto taskDto) {
    log.info("updateTaskNew input values:{}\nTaskDto value: {}", task, taskDto);
    var freshTask = taskDto;
    task.setName(taskDto.getName());
    task.setDescription(taskDto.getDescription());
    task.setStatus(taskDto.getStatus());
    task.setPriority(taskDto.getPriority());
    task.setDueDate(LocalDateTime.parse(taskDto.getDueDate()));
    task.setProgress(taskDto.getProgress());
//        task.setProjects(taskDto.getProjectName());
    if (taskDto.getProgress() >= 10 && task.getStatus() == Status.TODO) {
      task.setStatus(Status.IN_PROGRESS);
    }
    if (taskDto.getProgress() == 100) {
      if (task.getStatus() == Status.IN_PROGRESS || task.getStatus() == Status.PAUSE) {
        task.setStatus(Status.COMPLETE);
      }
    }
    taskDao.save(task);
    return task;
  }

  // TODO - not used , need to delete this method
  @Override
  public Task updateTask(Task freshTask) {
    log.info("updateTask input values:{}", freshTask);
    var oldTask = this.getTask(freshTask);
    if (oldTask == null) {
      log.error("This task {} not found", freshTask);
      throw new NullPointerException();
    } else {
      log.info("This task {} was update", freshTask);
      oldTask.setName(freshTask.getName());
      oldTask.setPriority(freshTask.getPriority());
      oldTask.setStatus(freshTask.getStatus());
      oldTask.setDescription(freshTask.getDescription());
      oldTask.setDueDate(freshTask.getDueDate());
      oldTask.setProgress(freshTask.getProgress());
      oldTask.setProjects(freshTask.getProjects());
      if (freshTask.getProgress() >= 10 && oldTask.getStatus() == Status.TODO) {
        oldTask.setStatus(Status.IN_PROGRESS);
      }
      if (freshTask.getProgress() == 100) {
        if (oldTask.getStatus() == Status.IN_PROGRESS || oldTask.getStatus() == Status.PAUSE) {
          oldTask.setStatus(Status.COMPLETE);
        }
      }
      taskDao.save(oldTask);
    }
    return freshTask;
  }

  @Override
  public Task getTask(Long id) {
    log.info("getTask input values:{}", id);
    if (id == null) {
      log.error("Id {} not found", id);
      throw new NullPointerException();
    } else {
      return taskDao.findById(id).orElse(null);
    }
  }

  //TODO - not used
  @Override
  public Task getTask(Task task) {
    log.info("getTask input values:{}", task);
    return this.getTask(task.getId());
  }


  // TODO - show tasks and find by name  method
  @Override
  public Page<Task> loadTask(PaginationDto paginationDto, String searchBy, String sortBy) {
    log.info("loadTasks method with paginationDto={} searchBy={} sortBy={}", paginationDto, searchBy, sortBy);
    searchBy = this.checkSearchBy(searchBy);
    sortBy = this.checkSortBy(sortBy);
    Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
    Page<Task> pt = taskDao.findAllBy(PageRequest.of(paginationDto.getIndex() - 1, paginationDto.getSize(), sort), searchBy);
    return pt;
  }

  @Override
  public void deleteTask(Long id) {
    log.info("delete input values:{}", id);
    this.taskDao.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    this.taskDao.deleteById(id);
  }

  private String checkSortBy(String sortBy) {
    log.info("Check sortBy={}", sortBy);
    if (sortBy == null || sortBy.isBlank()) {
      return "name";
    } else if (sortBy.equals("status")) {
      return "status";
    } else if (sortBy.equals("due_date")) {
      return "due_date";
    } else if (sortBy.equals("priority")) {
      return "priority";
    }
    return sortBy;
  }

  private String checkSearchBy(String searchBy) {
    log.info("Check searchBy={}", searchBy);
    if (searchBy == null || searchBy.isBlank()) {
      return "";
    }
    return searchBy;
  }

  @Override
  public Task addComment(Long taskId, Comment newComment) {
    log.info("addComment input values: task id {}, newComment {}", taskId, newComment);
    var task = getTask(taskId);
    if (task == null) {
      log.error("Task with id {} not found", taskId);
      throw new NullPointerException();
    } else {
      log.info("Task was found:{}", task);
      newComment.setCreated(LocalDateTime.now());
    }
    return this.addComment(task, newComment);
  }

  @Override
  public Task addComment(Task task, Comment newComment) {
    log.info("addComment input values: task {}, new Comment {}", task, newComment);
    if (task.getComments() == null) {
      List<Comment> comments = new ArrayList<>();
      comments.add(newComment);
      task.setComments(comments);
    } else {
      task.getComments().add(newComment);
    }
    newComment.setTask(task);
    commentDao.save(newComment);
    return task;
  }

  @Override
  public Comment removeComment(Long id) {
    log.info("removeComment input values:{}", id);
    Optional<Comment> optionalComment = commentDao.findById(id);
    if (optionalComment.isPresent()) {
      Comment c = optionalComment.get();
      commentDao.delete(optionalComment.get());
      return c;
    }
    return null;
  }

  @Override
  public Comment removeComment(Comment comment) {
    log.info("removeComment input values:{}", comment);
    return this.removeComment(comment.getId());
  }

  @Override
  public Comment updateComment(Comment freshComment) {
    log.info("updateComment input values:{}", freshComment);
    if (freshComment == null) {
      throw new NullPointerException("Comment cannot be null");
    } else {
      log.info("UpdateComment {} was update", freshComment);
      Optional<Comment> com = commentDao.findById(freshComment.getId());
      if (com.isPresent()) {
        Comment comment = com.get();
        comment.setText(freshComment.getText());
        commentDao.save(comment);
      }
    }
    return freshComment;
  }

  //index page load by due date
  public Collection<Task> sortedByDueDateDESCTasks(PaginationDto paginationDto) {
    log.info("sortedByNameDESCTasks input values: {}", paginationDto);
    return this.sortedBy(paginationDto, "dueDate");
  }

  @Override
  public Collection<Task> sortedBy(PaginationDto paginationDto, String sortBy) {
    log.info("sortedByNameASCTasks input values: {}", paginationDto);
    Sort sort = Sort.by(paginationDto.getSorDirectionASC(), sortBy);
    Page<Task> pt = taskDao.findAll(PageRequest.of(paginationDto.getPage(), paginationDto.getSize(), sort));
    return pt.getContent();
  }
}