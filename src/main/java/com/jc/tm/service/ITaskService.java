package com.jc.tm.service;

import com.jc.tm.db.entity.Comment;
import com.jc.tm.db.entity.Task;
import com.jc.tm.dto.PaginationDto;
import com.jc.tm.dto.TaskDto;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ITaskService {
    /**
     * save task entity into database.
     * @param newTask new entity wich will present task object
     * @return saved entity with generated id;
     */
    public Task createTask(Task newTask) ;

    /**
     * remove task from database. the task will be removed by id, but first method should find proper record into database
     * @param id identiifier for task
     * @return removed task object or null if task by id does not exists
     */
    public Task removeTask(Long id) ;

    /**
     * remove task from data base. this method calls @see removeTask(Long id)
     * @param task object task with identifier for monipulation
     * @return removed task object of null if task does not exists
     */
    public Task removeTask(Task task) ;

    public Task updateTaskStatus(Task freshTask, TaskDto status);

    public Task updateTaskNew(Task task, TaskDto taskDto);

    /**
     * update task. method do update or replace existing object into database. only identifier of object must be constant
     * @param freshTask new object or updated object of task
     * @return updated object of task
     */
    public Task updateTask(Task freshTask) ;

    /**
     * find task object by id
     * @param id idetnifier of task
     * @return Task object or null if object was not found
     */
    public Task getTask(Long id) ;

    /**
     * find object task. this method calls getTask(Long id)
     * @param task template for finding
     * @return task object
     */
    public Task getTask(Task task) ;


    public Task addComment(Long taskId, Comment newComment) ;
    public Task addComment(Task task, Comment newComment) ;

    public Comment removeComment(Long id) ;
    public Comment removeComment(Comment comment) ;
    public Comment updateComment(Comment freshComment) ;

    Collection<Task> sortedBy(PaginationDto paginationDto, String sortBy);

    Page<Task> loadTask(PaginationDto paginationDto, String searchBy, String sortBy);
}
