package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationsHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

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
    public void givenAValidEmptyDescription_whenCallNewCategory_thenInstantiateACategory()
    {
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedActive = true;

        final var actualCategory =
                Category.newCategory(expectedName,expectedDescription,expectedActive);

        Assertions.assertDoesNotThrow(()->actualCategory.validate(new ThrowsValidationsHandler()));

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
    public void givenAValidFalseIsActive_whenCallNewCategory_thenInstantiateACategory()
    {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = false;

        final var actualCategory =
                Category.newCategory(expectedName,expectedDescription,expectedActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnACategoryDeactivated()
    {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = false;

        final var aCategory =
                Category.newCategory(expectedName,expectedDescription,true);

        final var updatedAt = aCategory.getUpdatedAt();
        final var createdAt = aCategory.getCreatedAt();

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualCategory =aCategory.deactivate();

        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(actualCategory.getCreatedAt(),createdAt);
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdate_theReturnACategoryUpdated(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var aCategory =
                Category.newCategory("FIlm","expectedDescription",false);

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.update(expectedName,expectedDescription,expectedActive);


        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(actualCategory.getCreatedAt(),createdAt);
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateToInactive_theReturnACategoryUpdated(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = false;

        final var aCategory =
                Category.newCategory("FIlm","expectedDescription",true);

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.update(expectedName,expectedDescription,expectedActive);


        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(actualCategory.getCreatedAt(),createdAt);
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }
    @Test
    public void givenAValidInactiveCategory_whenCallActivate_thenReturnACategoryActivated()
    {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        final var aCategory =
                Category.newCategory(expectedName,expectedDescription,false);

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        final var actualCategory =aCategory.activate();

        Assertions.assertEquals(actualCategory.getId(),aCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(actualCategory.getCreatedAt(),createdAt);
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReturnAnError()
    {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedErrorMessage = "'Name' should not be null";

        final var actualCategory =
                Category.newCategory(expectedName,expectedDescription,expectedActive);

        final var actualExceptions = Assertions.assertThrows(DomainException.class,()-> actualCategory.validate(new ThrowsValidationsHandler()));

        Assertions.assertEquals(expectedErrorMessage,actualExceptions.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReturnAnError()
    {
        final String expectedName = "  ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedErrorMessage = "'Name' should not be empty";

        final var actualCategory =
                Category.newCategory(expectedName,expectedDescription,expectedActive);

        final var actualExceptions = Assertions.assertThrows(DomainException.class,()-> actualCategory.validate(new ThrowsValidationsHandler()));

        Assertions.assertEquals(expectedErrorMessage,actualExceptions.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReturnAnError()
    {
        final String expectedName = "fi ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedErrorMessage = "'Name' must be betweeen 3 and 255 characters";

        final var actualCategory =
                Category.newCategory(expectedName,expectedDescription,expectedActive);

        final var actualExceptions = Assertions.assertThrows(DomainException.class,()-> actualCategory.validate(new ThrowsValidationsHandler()));

        Assertions.assertEquals(expectedErrorMessage,actualExceptions.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthGreaterThan255_whenCallNewCategoryAndValidate_thenShouldReturnAnError()
    {
        final String expectedName = "Gostaria de enfatizar que a consulta aos diversos militantes deve passar por modificações independentemente das direções preferenciais no sentido do progresso. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se o desenvolvimento contínuo de distintas formas de atuação cumpre um papel essencial na formulação das condições financeiras e administrativas exigidas. Percebemos, cada vez mais, que a consolidação das estruturas obstaculiza a apreciação da importância das condições inegavelmente apropriadas. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a execução dos pontos do programa oferece uma interessante oportunidade para verificação dos índices pretendidos.";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;
        final var expectedErrorMessage = "'Name' must be betweeen 3 and 255 characters";

        final var actualCategory =
                Category.newCategory(expectedName,expectedDescription,expectedActive);

        final var actualExceptions = Assertions.assertThrows(DomainException.class,()-> actualCategory.validate(new ThrowsValidationsHandler()));

        Assertions.assertEquals(expectedErrorMessage,actualExceptions.getErrors().get(0).message());
    }

}
