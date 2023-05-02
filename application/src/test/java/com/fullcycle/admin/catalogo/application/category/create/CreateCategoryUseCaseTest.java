package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidComand_whenCallsCreateCategory_shouldReturnCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutPut = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutPut);
        Assertions.assertNotNull(actualOutPut.id());

        verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(aCategory ->
                {
                    return Objects.equals(expectedName,aCategory.getName())
                            && Objects.equals(expectedDescription, aCategory.getDescription())
                            && Objects.equals(expectedIsActive, aCategory.isActive())
                            && Objects.nonNull(aCategory.getCreatedAt())
                            && Objects.nonNull(aCategory.getUpdatedAt())
                            && Objects.nonNull(aCategory.getId())
                            && Objects.isNull(aCategory.getDeletedAt());


                }
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_shouldReturnDomainExceptions(){
        final String expectedName = null;
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'Name' should not be null";
        final var expectedErrorCount = "";

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        final var actualExcepetion =
                Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualExcepetion.getMessage());

        verify(categoryGateway, times(0)).create(any());
    }


    @Test
    public void givenAnInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = false;


        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutPut = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutPut);
        Assertions.assertNotNull(actualOutPut.id());

        verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(aCategory ->
                {
                    return Objects.equals(expectedName,aCategory.getName())
                            && Objects.equals(expectedDescription, aCategory.getDescription())
                            && Objects.equals(expectedIsActive, aCategory.isActive())
                            && Objects.nonNull(aCategory.getCreatedAt())
                            && Objects.nonNull(aCategory.getUpdatedAt())
                            && Objects.nonNull(aCategory.getId())
                            && Objects.nonNull(aCategory.getDeletedAt());
                }
        ));
    }

    @Test
    public void givenAValidComand_whenGatewayThrowsRandomExpection_shouldReturnAException(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = "";

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        when(categoryGateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException("Gateway Error"));

        final var actualExcepetion =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualExcepetion.getMessage());


        verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(aCategory ->
                {
                    return Objects.equals(expectedName,aCategory.getName())
                            && Objects.equals(expectedDescription, aCategory.getDescription())
                            && Objects.equals(expectedIsActive, aCategory.isActive())
                            && Objects.nonNull(aCategory.getCreatedAt())
                            && Objects.nonNull(aCategory.getUpdatedAt())
                            && Objects.nonNull(aCategory.getId())
                            && Objects.isNull(aCategory.getDeletedAt());


                }
        ));
    }}
