package com.fullcycle.admin.catalogo.application.category.delete;

import com.fullcycle.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {
    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_souldBeOK(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais atigida", true);
        final var expectedId = aCategory.getId();

        Mockito.doNothing()
                .when(categoryGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnIValidId_whenCallsDeleteCategory_souldBeOK(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais atigida", true);
        final var expectedId = aCategory.getId();

        Mockito.doNothing()
                .when(categoryGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnIValidId_whenGatewayThrowsException_souldReturnException(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais atigida", true);
        final var expectedId = aCategory.getId();

        Mockito.doThrow(new IllegalStateException("Gateway Error"))
                .when(categoryGateway).deleteById(expectedId);

        Assertions.assertThrows(IllegalStateException.class,()->useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(eq(expectedId));
    }
}
