package br.ce.wcaquino.taskbackend.controller;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

public class TaskControllerTest {

    @Mock
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskController taskController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mustSaveNotOneTasksWithDescriptionMissing() {
        Task todo = new Task();
        todo.setDueDate(LocalDate.now());
        try {
            taskController.save(todo);
            Assert.fail("Security point reached");
        } catch(ValidationException e) {
            Assert.assertEquals("Fill the task description", e.getMessage());
        }
    }

    @Test
    public void mustSaveNotOneTasksWithDateMissing() {
        Task todo = new Task();
        todo.setTask("Task to test");
        try {
            taskController.save(todo);
            Assert.fail("Security point reached");
        } catch(ValidationException e) {
            Assert.assertEquals("Fill the due date", e.getMessage());
        }
    }

    @Test
    public void mustSaveNotOneTasksWithOldDate() {
        Task todo = new Task();
        todo.setTask("Task to test");
        todo.setDueDate(LocalDate.now().plusDays(-1));
        try {
            taskController.save(todo);
            Assert.fail("Security point reached");
        } catch(ValidationException e) {
            Assert.assertEquals("Due date must not be in past", e.getMessage());
        }
    }

    @Test
    public void mustSaveOneTasksSuccessful() throws ValidationException {
        Task todo = new Task();
        todo.setTask("Task to test");
        todo.setDueDate(LocalDate.now());
        taskController.save(todo);
        Mockito.verify(taskRepo).save(todo);
    }
    
}
