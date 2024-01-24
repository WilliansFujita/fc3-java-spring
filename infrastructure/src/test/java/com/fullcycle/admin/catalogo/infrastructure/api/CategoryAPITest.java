package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.CategoryOutPut;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryOutPut;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryAPIInput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryAPIInput;
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
import static org.mockito.Mockito.*;
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

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

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

    @Test
    public void givenAValidID_whenCallGetCategory_shouldReturnCategory() throws Exception {

        //given
        final String expectedName = "Filme";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutPut.from(aCategory));

        //when



        final var request = MockMvcRequestBuilders.get("/categories/{id}",expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = this.mvc.perform(request)
                .andDo(print());



        //then
        response.andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", equalTo(expectedId)))
                        .andExpect(jsonPath("$.name", equalTo(expectedName)))
                        .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                        .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                        .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                        .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                        .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidID_whenCallGetCategory_shouldReturnNotFound() throws Exception {
        //given
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.fromString("123");

        //when
        Mockito.when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class,expectedId));

        final var request = MockMvcRequestBuilders.get("/categories/{id}",expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var response = this.mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",equalTo(expectedErrorMessage)));
    }


    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryID() throws Exception {
        //given
        final var expectedId = "123";
        final String expectedName = "Filme";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;
        final var aCategory = Category.newCategory("Filme", null, true);


        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutPut.from(aCategory.getId().getValue())));

        final var aCommand = new UpdateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                        .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request);


        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(aCategory.getId().getValue())));

        verify(updateCategoryUseCase,times(1)).execute(argThat(cmd->
                Objects.equals(cmd.name(), expectedName)
                && Objects.equals(cmd.description(), expectedDescription)
                && Objects.equals(cmd.isActive(), expectedIsActive)
        ));

    }

    @Test
    public void givenAInvalidID_whenCallsUpdateCategory_shouldReturnotFoundException() throws Exception {
        //given
        final var expectedId = "123";
        final String expectedName = "Filme";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID 123 was not found";


        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.fromString(expectedId)));

        final var aCommand = new UpdateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request);


        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase,times(1)).execute(argThat(cmd->
                Objects.equals(cmd.name(), expectedName)
                        && Objects.equals(cmd.description(), expectedDescription)
                        && Objects.equals(cmd.isActive(), expectedIsActive)
        ));

    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_shouldDomainException() throws Exception {
        //given
        final var expectedId = "123";
        final String expectedName = "Filme";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedMessage = "'name' should not be null";


        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var aCommand = new UpdateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request);


        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(updateCategoryUseCase,times(1)).execute(argThat(cmd->
                Objects.equals(cmd.name(), expectedName)
                        && Objects.equals(cmd.description(), expectedDescription)
                        && Objects.equals(cmd.isActive(), expectedIsActive)
        ));

    }
}
