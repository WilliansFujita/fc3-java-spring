package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CreateCategoryUseCaseTestIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidComand_whenCallsCreateCategory_shouldReturnCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A Categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualOutPut = useCase.execute(aCommand).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertNotNull(actualOutPut);
        Assertions.assertNotNull(actualOutPut.id());

        CategoryJPAEntity actualCategory = categoryRepository.findById(actualOutPut.id().getValue()).get();

        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }
}
