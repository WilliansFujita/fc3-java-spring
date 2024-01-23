package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryAPIInput;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    public void givenAValidComand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;

        final var input = new CreateCategoryAPIInput(expectedName,expectedDescription,expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from(CategoryID.fromString("123"))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location","/categories/123"),
                        jsonPath("$.id", equalTo("123"))
                );

        verify(createCategoryUseCase,times(1))
                .execute(argThat(cmd->
                        Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_shouldReturnNotification() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var input = new CreateCategoryAPIInput(expectedName,expectedDescription,expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location",nullValue()),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage))
                );

        verify(createCategoryUseCase,times(1))
                .execute(argThat(cmd->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }


    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_shouldReturnDomainException() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var input = new CreateCategoryAPIInput(expectedName,expectedDescription,expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location",nullValue()),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage))
                );

        verify(createCategoryUseCase,times(1))
                .execute(argThat(cmd->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }
}
