package com.jc.tm.controller;

import com.jc.tm.converter.Converter;
import com.jc.tm.util.Status;
import com.jc.tm.db.entity.Comment;
import com.jc.tm.db.entity.Task;
import com.jc.tm.util.Priority;
import com.jc.tm.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(Dashboard.class)
class DashboardTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskServiceImpl taskServiceMock;
  @MockBean
  private Converter converter;

//    @Before
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new Dashboard(taskServiceMock, converter)).build();
//    }

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
