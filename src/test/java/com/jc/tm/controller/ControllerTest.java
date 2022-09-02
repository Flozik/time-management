package com.jc.tm.controller;

import com.jc.tm.converter.Converter;
import com.jc.tm.db.entity.Project;
import com.jc.tm.db.entity.Task;
import com.jc.tm.dto.TaskDto;
import com.jc.tm.service.impl.ProjectServiceImpl;
import com.jc.tm.service.impl.TaskServiceImpl;
import com.jc.tm.util.Priority;
import com.jc.tm.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@RunWith(SpringRunner.class)
//@WebMvcTest(Controller.class)
@ExtendWith(MockitoExtension.class)
public class ControllerTest {

  private final TaskServiceImpl taskService = mock(TaskServiceImpl.class);
  private final ProjectServiceImpl projectService = mock(ProjectServiceImpl.class);
  private final Converter converter = new Converter();
  private final Controller controller = new Controller(taskService, projectService, converter);
  private MockMvc mockMvc;

  @BeforeEach
  public void setMockMvc() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void mainPageTest() throws Exception {
    this.mockMvc.perform(
                    MockMvcRequestBuilders.get("/"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotHaveJsonPath());
  }

  @Test
  void createTaskTest() throws Exception {
    Project project = new Project();
    project.setName("Test project");
    Task task = new Task();
    task.setId(1L);
    task.setName("Name");
    task.setDescription("Some description");
    task.setStatus(Status.IN_PROGRESS);
    task.setPriority(Priority.HIGH);
    task.setProgress(10);
    task.setProjects(project);

    when(taskService.createTask(any(Task.class))).thenReturn(task);

    this.mockMvc.perform(
                    MockMvcRequestBuilders.post("/create-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"Just send\": \"some information\"}"))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value("1"))
            .andExpect(jsonPath("name").value("Name"))
            .andExpect(jsonPath("description").value("Some description"))
            .andExpect(jsonPath("status").value("IN_PROGRESS"))
            .andExpect(jsonPath("priority").value("HIGH"))
            .andExpect(jsonPath("progress").value("10"))
            .andExpect(jsonPath("projects").value(project));

    verify(taskService).createTask(any(Task.class));
  }

  @Test
  void updateTaskTest() throws Exception {
    final Long taskId = 12345L;
    Task task = new Task();
    task.setId(taskId);
    task.setName("Some name");
    task.setDescription("Some description");
    /*TaskDto taskDto = new TaskDto();
    taskDto.setName("New name");*/
    Task updatedTask = new Task();
    updatedTask.setName("New name");
    when(taskService.getTask(taskId)).thenReturn(task);
    when(taskService.updateTaskNew(eq(task), any(TaskDto.class))).thenReturn(updatedTask);

    this.mockMvc.perform(
                    MockMvcRequestBuilders.post("/task/update/{taskId}", taskId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"some information\"}"))
            .andDo(print())
            .andExpect(status().isOk());
  }

  @Test
  void deleteTaskTest() throws Exception {
    final Long taskId = 12345L;
    Task task = new Task();
    task.setId(taskId);

    when(taskService.removeTask(eq(taskId))).thenReturn(task);

    String expectedResponse = "{\"id\":12345,\"name\":null,\"description\":null," +
            "\"created\":null,\"comments\":null,\"status\":\"TODO\",\"dueDate\":null," +
            "\"priority\":\"NORMAL\",\"progress\":0,\"projects\":null}";

    this.mockMvc.perform(
                    MockMvcRequestBuilders.get("/delete-task/{taskId}", taskId)
                            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResponse));

    verify(taskService).removeTask(eq(taskId));
  }

  private String dateConverter(LocalDateTime time) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    String date = time.format(dateTimeFormatter);
    return date;
  }

    /*@Test
    void shouldGetTaskById() throws Exception {
        Comment comment1 = Comment.builder().setId(1L).setText("comment 1").setCreated(LocalDateTime.now()).build();
        Comment comment2 = Comment.builder().setId(2L).setText("comment 2").setCreated(LocalDateTime.now()).build();
        List<Comment> comments = Arrays.asList(comment1, comment2);

        Task foundTask = Task.builder().setId(1L).setName("test").setDescription("some description")
                .setStatus(Status.TODO).setPriority(Priority.NORMAL)
                .setCreated(LocalDateTime.of(2021, 10, 19, 12, 25, 25))
                .setDueDate(LocalDateTime.of(2021, 11, 19, 12, 25, 25))
                .setComments(comments)
                .build();

        doCallRealMethod().when(converter).taskToTaskDto(foundTask);
        when(taskServiceMock.getTask(1L)).thenReturn(foundTask);

        mockMvc.perform(get("/task/{taskId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("task"))
                .andExpect(model().attribute("task", hasProperty("id", is(1L))))
                .andExpect(model().attribute("task", hasProperty("name", is("test"))))
                .andExpect(model().attribute("task", hasProperty("description", is("some description"))))
                .andExpect(model().attribute("task", hasProperty("status", is(Status.TODO))))
                .andExpect(model().attribute("task", hasProperty("priority", is(Priority.NORMAL))))
                .andExpect(model().attribute("task", hasProperty("created", is(dateConverter(LocalDateTime.of(2021, 10, 19, 12, 25, 25))))))
                .andExpect(model().attribute("task", hasProperty("dueDate", is(dateConverter(LocalDateTime.of(2021, 11, 19, 12, 25, 25))))))
                .andExpect(model().attribute("comments", hasSize(2)))
                .andExpect(model().attribute("comments", hasItem(
                                allOf(
                                        hasProperty("id", is(1L)),
                                        hasProperty("text", is("comment 1")),
                                        hasProperty("created", is(dateConverter(LocalDateTime.now())))))
                        )
                )
                .andExpect(model().attribute("comments", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("text", is("comment 2")),
                                hasProperty("created", is(dateConverter(LocalDateTime.now()))))
                )));

        verify(taskServiceMock, times(1)).getTask(1L);
        verify(converter, times(1)).taskToTaskDto(foundTask);
        verifyNoMoreInteractions(taskServiceMock);
    }

    @Test
    void showEditTask() throws Exception {
        Task showingTask = Task.builder().setId(1L).setName("test").setDescription("some description").setStatus(Status.TODO).setPriority(Priority.NORMAL)
                .setCreated(LocalDateTime.of(2021, 10, 19, 12, 25, 25))
                .setDueDate(LocalDateTime.of(2021, 11, 19, 12, 25, 25))
                .build();

        when(taskServiceMock.getTask(1L)).thenReturn(showingTask);

        mockMvc.perform(get("/task/edit/{taskId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("update-task"))
                .andExpect(model().attribute("task", hasProperty("id", is(1L))))
                .andExpect(model().attribute("task", hasProperty("name", is("test"))))
                .andExpect(model().attribute("task", hasProperty("description", is("some description"))))
                .andExpect(model().attribute("task", hasProperty("status", is(Status.TODO))))
                .andExpect(model().attribute("task", hasProperty("priority", is(Priority.NORMAL))))
                .andExpect(model().attribute("task", hasProperty("created", is(LocalDateTime.of(2021, 10, 19, 12, 25, 25)))))
                .andExpect(model().attribute("task", hasProperty("dueDate", is(LocalDateTime.of(2021, 11, 19, 12, 25, 25)))));

        verify(taskServiceMock, times(1)).getTask(1L);
        verifyNoMoreInteractions(taskServiceMock);
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(get("/create-task"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-task"))
                .andExpect(model().attribute("task", hasProperty("status", is(Status.TODO))))
                .andExpect(model().attribute("task", hasProperty("priority", is(Priority.NORMAL))));
    }

    @Test
    void createTask() throws Exception {
        mockMvc.perform(post("/add-task"))
                .andExpect(status().is3xxRedirection());
    }*/
}
