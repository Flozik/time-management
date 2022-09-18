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
    log.info("Show last five tasks");
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
    log.info("Controller. createTask. Task:{}", task);
    return this.service.createTask(task);
  }

  @GetMapping("show-tasks/page/{pageNumber}")
  public Collection<TaskDto> show(String searchBy,
                                  @PathVariable(value = "pageNumber") int pageNumber,
                                  @RequestParam(name = "sortBy", required = false) String sortBy) {
    log.info("Controller. show. SearchBy:{}, pageNumber:{}, sortBy:{}", searchBy, pageNumber, sortBy);
    PaginationDto paginationDto = new PaginationDto();
    paginationDto.setIndex(pageNumber);
    paginationDto.setSize(pageSize);
    var taskList = service.loadTask(paginationDto, searchBy, sortBy);
    return converter.parsingTaskDataToTaskDTO(taskList.getContent());
  }

  @GetMapping("show-tasks/searchBy={searchBy}")
  public Collection<TaskDto> findByName(@PathVariable String searchBy) {
    log.info("Find task by name with value = {}", searchBy);
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
    log.info("Controller. getTaskById. taskId:{}", taskId);
    return this.service.getTask(taskId);
  }

  //TODO - DONE
  @PostMapping(value = {"show-tasks/task/update/{taskId}"})
  public TaskDto updateTaskStatus(@PathVariable long taskId, @RequestBody TaskDto status) {
    log.info("Controller. updateTaskStatus. id:{} status:{}", taskId, status);
    return this.service.updateTaskStatus(taskId, status);
  }

  //TODO = DONE
  @PostMapping(value = {"/task/update/{taskId}"})
  public TaskDto updateTask(@PathVariable long taskId,
                         @Valid @RequestBody TaskDto taskDto) {
    log.info("Controller. updateTask. id:{}, taskDto:{}", taskId, taskDto);
    return this.service.updateTask(taskId, taskDto);
  }

  @PostMapping("/delete-task/{taskId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteTask(@PathVariable long taskId) {
    log.info("Controller. deleteTask. id:{}", taskId);
    service.deleteTask(taskId);
  }

  //Comment controllers

  @PostMapping("/task/{taskId}/added-comment")
  public String addComment(@ModelAttribute("comment") Comment comment, @PathVariable("taskId") long taskId) {
    log.info("Add comment={} in task with id={}", comment, taskId);
    service.addComment(taskId, comment);
    return "redirect:/task/" + taskId;
  }

  @PostMapping("/task/{taskId}/edit-comment/{commentId}")
  public String editComment(@ModelAttribute("comment") Comment comment,
                            @PathVariable("commentId") long commentId,
                            @PathVariable("taskId") long taskId) {
    log.info("Update comment with id={} in task with id={}", commentId, taskId);
    comment.setId(commentId);
    service.updateComment(comment);
    return "redirect:/task/" + taskId;
  }


  @GetMapping("/task/{taskId}/comment-del/{commentId}")
  public String deleteComment(@PathVariable("commentId") long commentId, @PathVariable("taskId") long taskId) {
    log.info("Delete comment with id={} in task with id={}", commentId, taskId);
    service.removeComment(commentId);
    return "redirect:/task/" + taskId;
  }

  //Project controllers

  //TODO - DONE
  @PostMapping("/add-project")
  public Project addProject(@RequestBody Project project) {
    log.info("Add project page. Project={}", project);
    return projectService.saveProject(project);
  }

  //TODO - DONE
  @GetMapping("/get-all-projects")
  public Collection<ProjectDto> loadProjects() {
    log.info("loading all projects");
    return converter.parsingProjectDataToProjectDTO(projectService.loadProject());
  }
}