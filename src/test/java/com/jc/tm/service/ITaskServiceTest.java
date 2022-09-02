/*
package com.jc.tm.service;

import com.jc.tm.service.impl.TaskServiceImpl;
import com.jc.tm.util.Status;
import com.jc.tm.db.dao.jpa.CommentDao;
import com.jc.tm.db.dao.jpa.TaskDao;
import com.jc.tm.db.entity.Comment;
import com.jc.tm.db.entity.Task;
import com.jc.tm.dto.PaginationDto;
import com.jc.tm.util.Priority;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ITaskServiceTest {

  @Mock
  private TaskDao taskDao;
  @Mock
  private CommentDao commentDao;

  @InjectMocks
  private TaskServiceImpl taskService;

  @BeforeEach
  void setUp() {
  }

  @Test
  @DisplayName("Positive test for save task")
  void saveTask() {
    //given
    Task task = new Task();
    task.setId(1L);
    task.setName("test");
    task.setDescription("some description");
    task.setStatus(Status.TODO);
    task.setPriority(Priority.NORMAL);
    //when
    when(taskDao.save(any(Task.class))).thenReturn(task);
    //then
    task = taskService.saveTask(task);
    assertThat(task).isNotNull();
    assertNotNull(task.getId());
  }

  @Test
  @DisplayName("Positive test for remove task")
  void removeTaskPositive() {
    Task task = new Task();
    Long id = 1L;
    task.setId(id);
    when(taskDao.findById(id)).thenReturn(Optional.of(task));
    task = taskService.removeTask(task);
    assertNotNull(task);
    verify(taskDao, atLeastOnce()).delete(any(Task.class));
  }

  @Test
  @DisplayName("Negative test for remove task")
  public void removeTaskNegativeTest() {
    Task task = new Task();
    task.setId(1L);
    when(taskDao.findById(anyLong())).thenReturn(Optional.empty());
    task = taskService.removeTask(2L);
    assertNull(task);
    verify(taskDao, never()).delete(any(Task.class));
  }

  @Test
  @DisplayName("throw new NullPointerException() in id is null")
  public void throwsNullPointerIfIdIsNull() {
    Long id = null;
    assertThatThrownBy(() -> taskService.getTask(id)).isInstanceOf(NullPointerException.class);
  }

  //not sure i wrote this test right
  @Test
  @DisplayName("Update task if exists")
  public void shouldUpdateTaskIfExists() {
    Task task = new Task();
    task.setId(1L);
    task.setName("updated task");
    when(taskDao.findById(anyLong())).thenReturn(Optional.of(task));
    task = taskService.updateTask(task);
    assertNotNull(task);
  }

  @Test
  @DisplayName("Load all tasks - check if the method findAll was called one time")
  void loadAllTasks() {
    taskService.loadTasks();
    verify(taskDao, atLeastOnce()).findAll();
  }

  @Test
  @DisplayName("Add comment test")
  public void addComment() {
    Task task = new Task();
    Long id = 1L;
    task.setId(id);
    Comment comment = new Comment();
    comment.setId(id);
    comment.setText("new comment");
    when(taskDao.findById(id)).thenReturn(Optional.of(task));
    Task savedTask = taskService.addComment(id, comment);
    Assertions.assertTrue(savedTask.getComments().contains(comment));
    verify(commentDao, atLeastOnce()).save(any(Comment.class));
  }

  @Test
  @DisplayName("Add comment to present comments")
  public void addComment_areCommentsExists() {
    Comment com = new Comment();
    List<Comment> c = new ArrayList<>();
    c.add(com);
    Task task = new Task();
    Long id = 1L;
    task.setId(id);
    task.setComments(c);
    Comment comment = new Comment();
    comment.setId(id);
    comment.setText("new comment");
    when(taskDao.findById(id)).thenReturn(Optional.of(task));
    Task savedTask = taskService.addComment(id, comment);
    Assertions.assertTrue(savedTask.getComments().contains(comment));
    Assertions.assertEquals(2, savedTask.getComments().size());
    verify(commentDao, atLeastOnce()).save(any(Comment.class));
  }

  @Test
  @DisplayName("updateTask")
  public void updateTask() {
    Task task = new Task();
    Long id = 1L;
    task.setId(id);
    task.setName("Okay");
    when(taskDao.findById(id)).thenReturn(Optional.of(task));
    when(taskService.updateTask(task)).thenReturn(task);
    task = taskService.updateTask(task);
    assertNotNull(task);
    Assertions.assertNull(task.getDescription());
    assertNotNull(task.getName());
    assertEquals(task.getName(), "Okay");
  }

  @Test
  @DisplayName("setDueDate")
  public void setDueDate() {
    Task task = new Task();
    LocalDateTime localDateTime = LocalDateTime.parse("2021-06-01T00:00:00");
    Long id = 1L;
    task.setId(id);
    task.setDueDate(localDateTime);
    taskService.saveTask(task);
    when(taskDao.findById(id)).thenReturn(Optional.of(task));
    when(taskService.setDueDate(task, localDateTime)).thenReturn(task);
    task = taskService.setDueDate(task, localDateTime);
    assertNotNull(task.getDueDate());
    assertEquals(localDateTime, task.getDueDate());
  }

  @Test
  @DisplayName("updateDueDate")
  public void updateDueDate() {
    Task task = new Task();
    LocalDateTime localDateTime = LocalDateTime.parse("2021-08-01T00:00:00");
    Long id = 1L;
    task.setId(id);
    task.setDueDate(localDateTime);
    when(taskDao.findById(id)).thenReturn(Optional.of(task));
    when(taskService.updateDueDate(task, localDateTime)).thenReturn(task);
    task = taskService.updateDueDate(task, localDateTime);
    assertEquals(localDateTime, task.getDueDate());
    verify(taskDao, atLeastOnce()).findById(id);
  }

  @Test
  @DisplayName("setPriority")
  public void setPriority() {
    Task task = new Task();
    Long id = 1L;
    task.setId(id);
    when(taskDao.findById(id)).thenReturn(Optional.of(task));
    when(taskService.setPriority(task, Priority.PAUSE)).thenReturn(task);
    task = taskService.setPriority(task, Priority.PAUSE);
    assertEquals(Priority.PAUSE, task.getPriority());
  }

  @Test
  @DisplayName("removeComment")
  public void removeComment() {
    Task task = new Task();
    Long id = 1L;
    task.setId(id);
    Comment comment = new Comment();
    comment.setId(id);
    comment.setText("Some text");
    when(commentDao.findById(id)).thenReturn(Optional.of(comment));
    doNothing().when(commentDao).delete(comment);
    comment = taskService.removeComment(id);
    assertNotNull(comment);
    verify(commentDao, atLeastOnce()).delete(comment);
  }

  @Test
  @DisplayName("updateComment")
  public void updateComment() {
    Task task = new Task();
    Comment comment = new Comment();
    Long id = 1L;
    task.setId(id);
    comment.setId(id);
    comment.setText("New text");
    when(commentDao.findById(id)).thenReturn(Optional.of(comment));
    when(taskService.updateComment(comment)).thenReturn(comment);
    comment = taskService.updateComment(comment);
    assertNotNull(task);
    assertNotNull(comment);
    assertEquals("New text", comment.getText());
  }

  @Test
  @DisplayName("Positive loadTask")
  void loadTaskPositive() {
    PaginationDto paginationDto = new PaginationDto();
    paginationDto.setIndex(1);
    paginationDto.setSize(10);
    String searchBy = null;
    String sortBy = null;
    Task task = new Task();
    task.setId(1L);
    Task task2 = new Task();
    task2.setId(2L);
    List<Task> list = new ArrayList<>();
    list.add(task);
    list.add(task2);
    Page<Task> result = new PageImpl<>(list);
    Page<Task> newResult;
    when(taskDao.findAllBy(any(Pageable.class), anyString())).thenReturn(result);
    newResult = taskService.loadTask(paginationDto, searchBy, sortBy);
    assertNotNull(newResult);
    verify(taskDao, atLeastOnce()).findAllBy(any(Pageable.class), anyString());
  }
}
*/
