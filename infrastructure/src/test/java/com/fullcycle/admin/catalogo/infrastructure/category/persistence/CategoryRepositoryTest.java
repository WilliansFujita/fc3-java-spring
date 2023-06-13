package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.PostgresGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@PostgresGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNameNull_whensCallsSave_shouldReturnError(){
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.name";

        final var aCategory = Category.newCategory("Nova Categoria", "A Categoria descrição", true);

        CategoryJPAEntity anEntity = CategoryJPAEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,()->categoryRepository.save(anEntity));
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,actualException.getCause());

        Assertions.assertEquals(expectedPropertyName,actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage,actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidCreatedAtNull_whensCallsSave_shouldReturnError(){
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity.createdAt";

        final var aCategory = Category.newCategory("Nova Categoria", "A Categoria descrição", true);

        CategoryJPAEntity anEntity = CategoryJPAEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,()->categoryRepository.save(anEntity));
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,actualException.getCause());

        Assertions.assertEquals(expectedPropertyName,actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage,actualCause.getMessage());
    }


}
