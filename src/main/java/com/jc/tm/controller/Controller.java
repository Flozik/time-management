package com.jc.tm.controller;

import com.jc.tm.converter.Converter;
import com.jc.tm.db.entity.Comment;
import com.jc.tm.db.entity.Project;
import com.jc.tm.db.entity.Task;
import com.jc.tm.dto.PaginationDto;
import com.jc.tm.dto.TaskDto;
import com.jc.tm.service.impl.TaskServiceImpl;
import com.jc.tm.dto.ProjectDto;
import com.jc.tm.service.impl.ProjectServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * this class is controller and it merge database with UI
 */

@Slf4j
@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/")
public class Controller {
  private final int pageSize = 10;

  private final TaskServiceImpl service;
  private final ProjectServiceImpl projectService;
  private final Converter converter;

  @Autowired
  public Controller(TaskServiceImpl service, ProjectServiceImpl projectService, Converter converter) {
    this.service = service;
    this.projectService = projectService;
    this.converter = converter;
  }

  @GetMapping
  public Collection<TaskDto> mainPage() {
    log.debug("Show last five tasks");
    PaginationDto paginationDto = new PaginationDto();
    paginationDto.setPage(0);
    paginationDto.setSize(5);
    Collection<Task> taskList = service.sortedByDueDateDESCTasks(paginationDto);
    return converter.parsingTaskDataToTaskDTO(taskList);
  }

  //TODO - DONE
  @PostMapping("/create-task")
  @ResponseStatus(HttpStatus.CREATED)
  public Task createTask(@RequestBody Task task) {
    log.debug("Create task page");
    return this.service.createTask(task);
  }

  @GetMapping("show-tasks/page/{pageNumber}")
  public Collection<TaskDto> show(String searchBy,
                                  @PathVariable(value = "pageNumber") int pageNumber,
                                  @RequestParam(name = "sortBy", required = false) String sortBy) {
    log.debug("Show tasks page with params: searchBy={}, pageNumber={}, sortBy={}", searchBy, pageNumber, sortBy);
    PaginationDto paginationDto = new PaginationDto();
    paginationDto.setIndex(pageNumber);
    paginationDto.setSize(pageSize);
    var taskList = service.loadTask(paginationDto, searchBy, sortBy);
    var result = converter.parsingTaskDataToTaskDTO(taskList.getContent());
    return result;
  }

  @GetMapping("show-tasks/searchBy={searchBy}")
  public Collection<TaskDto> findByName(@PathVariable String searchBy) {
    log.debug("Find task by name with value = {}", searchBy);
    String sortBy = "";
    int pageNumber = 1;
    PaginationDto paginationDto = new PaginationDto();
    paginationDto.setIndex(pageNumber);
    paginationDto.setSize(pageSize);
    var taskList = service.loadTask(paginationDto, searchBy, sortBy);
    return converter.parsingTaskDataToTaskDTO(taskList.getContent());
  }

  //TODO - DONE
  @GetMapping("/task/{taskId}")
  public TaskDto getTaskById(@PathVariable long taskId) {
    log.debug("Show one task with id={}", taskId);
    Task task = service.getTask(taskId);
    return converter.taskToTaskDto(task);
  }

  //TODO - DONE
  @PostMapping(value = {"show-tasks/task/update/{taskId}"})
  public Task updateTaskStatus(@PathVariable long taskId, @RequestBody TaskDto status) {
    log.debug("Update TasksTableRows Status: {}", status);
    Task task = service.getTask(taskId);
    return service.updateTaskStatus(task, status);
  }

  //TODO = DONE
  @PostMapping(value = {"/task/update/{taskId}"})
  public Task updateTask(@PathVariable long taskId, @Valid @RequestBody TaskDto taskDto) {
    log.info("Update task={} with id={}", taskDto, taskId);
    Task task = service.getTask(taskId);
    return service.updateTaskNew(task, taskDto);
  }

  //TODO - DONE
  @GetMapping("/delete-task/{taskId}")
  @ResponseStatus(HttpStatus.OK)
  public Task deleteTask(@PathVariable long taskId) {
    log.debug("Delete task with id={}", taskId);
    return service.removeTask(taskId);
  }

  //Comment controllers

  @PostMapping("/task/{taskId}/added-comment")
  public String addComment(@ModelAttribute("comment") Comment comment, @PathVariable("taskId") long taskId) {
    log.debug("Add comment={} in task with id={}", comment, taskId);
    service.addComment(taskId, comment);
    return "redirect:/task/" + taskId;
  }

  @PostMapping("/task/{taskId}/edit-comment/{commentId}")
  public String editComment(@ModelAttribute("comment") Comment comment,
                            @PathVariable("commentId") long commentId,
                            @PathVariable("taskId") long taskId) {
    log.debug("Update comment with id={} in task with id={}", commentId, taskId);
    comment.setId(commentId);
    service.updateComment(comment);
    return "redirect:/task/" + taskId;
  }


  @GetMapping("/task/{taskId}/comment-del/{commentId}")
  public String deleteComment(@PathVariable("commentId") long commentId, @PathVariable("taskId") long taskId) {
    log.debug("Delete comment with id={} in task with id={}", commentId, taskId);
    service.removeComment(commentId);
    return "redirect:/task/" + taskId;
  }

  //Project controllers

  //TODO - DONE
  @PostMapping("/add-project")
  public Project addProject(@RequestBody Project project) {
    log.debug("Add project page. Project={}", project);
    return projectService.saveProject(project);
  }

  //TODO - DONE
  @GetMapping("/get-all-projects")
  public Collection<ProjectDto> loadProjects() {
    log.debug("loading all projects");
    return converter.parsingProjectDataToProjectDTO(projectService.loadProject());
  }
}