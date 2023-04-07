package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationsHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory()
    {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var actualCategory =
                Category.newCategory(expectedName,expectedDescription,expectedActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReturnAnError()
    {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedErrorMessage = "'Name should not be null'";

        final var actualCategory =
                Category.newCategory(expectedName,expectedDescription,expectedActive);

        final var actualExceptions = Assertions.assertThrows(DomainException.class,()-> actualCategory.validate(new ThrowsValidationsHandler()));

        Assertions.assertEquals(expectedErrorMessage,actualExceptions.getErrors().get(0).message());
    }
}
