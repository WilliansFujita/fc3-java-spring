package com.fullcycle.admin.catalogo.application.category.delete;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class DeleteCategoryUseCaseITTest {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway gateway;



    @Test
    public void givenAnIValidId_whenCallsDeleteCategory_souldBeOK(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais atigida", true);
        final var expectedId = aCategory.getId();

        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, Mockito.times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnIValidId_whenGatewayThrowsException_souldReturnException(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais atigida", true);
        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway Error"))
                .when(gateway).deleteById(expectedId);

        Assertions.assertThrows(IllegalStateException.class,()->useCase.execute(expectedId.getValue()));

        Mockito.verify(gateway, Mockito.times(1)).deleteById(eq(expectedId));
    }
}
