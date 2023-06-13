package com.fullcycle.admin.catalogo.infrastructure.category;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.PostgresGatewayTest;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PostgresGatewayTest
public class CategoryPostgresGatewayTest {

    @Autowired
    private CategoryPostgresGateway categoryPostgresGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryPostgresGateway.create(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId().getValue(), actualCategory.getId().getValue());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());

    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnANewCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        Category categoryUpdated = aCategory.clone().update(expectedName, expectedDescription, expectedIsActive);
        final var actualCategory = categoryPostgresGateway.update(categoryUpdated);


        Assertions.assertEquals(aCategory.getId().getValue(), actualCategory.getId().getValue());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());

    }

    @Test
    public void giveAPrePersistedCategory_whenCallsDelete_shouldDeleteCategory(){
        Category aCategory = Category.newCategory("Filmes", null, true);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        categoryPostgresGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0, categoryRepository.count());

    }

    @Test
    public void givenAnInvalidId_whenTryDeleteIt_ShouldDeleteCategory(){

        Assertions.assertEquals(0, categoryRepository.count());
        categoryPostgresGateway.deleteById(CategoryID.fromString("invalid"));
        Assertions.assertEquals(0, categoryRepository.count());

    }

    @Test
    public void givenAValidCategory_whenCallsFindById_shouldReturnCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());

    }
    @Test
    public void givenAValidCategoryNotStored_whenCallsFindById_shouldReturnEmpty(){

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryRepository.findById(CategoryID.fromString("empty").getValue());

        Assertions.assertTrue(actualCategory.isEmpty());

    }

    @Test
    public void givenAValidPrePersistedCategories_whenCallsFindAll_ShoulReturnPaginated(){
        final var expectedPage = 0 ;
        final var expectedPerPage = 1 ;
        final var expectedTotal = 3 ;

        final var filmes = Category.newCategory("Filme", null,true);
        final var series = Category.newCategory("Séries", null,true);
        final var documentarios = Category.newCategory("Documentário", null,true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJPAEntity.from(filmes),
                CategoryJPAEntity.from(series),
                CategoryJPAEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        CategorySearchQuery searchQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        Pagination<Category> pagination = categoryPostgresGateway.findAll(searchQuery);

        Assertions.assertEquals(expectedPage, pagination.currentPage());
        Assertions.assertEquals(expectedPerPage, pagination.perPage());
        Assertions.assertEquals(expectedTotal, pagination.total());
        Assertions.assertEquals(expectedPerPage, pagination.items().size());
        Assertions.assertEquals(documentarios.getName(), pagination.items().get(0).getName());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_ShoulReturnEmptyPage(){

        final var expectedPage = 0 ;
        final var expectedPerPage = 1 ;
        final var expectedTotal = 0 ;

        Assertions.assertEquals(0, categoryRepository.count());

        CategorySearchQuery searchQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        Pagination<Category> pagination = categoryPostgresGateway.findAll(searchQuery);

        Assertions.assertEquals(expectedPage, pagination.currentPage());
        Assertions.assertEquals(expectedPerPage, pagination.perPage());
        Assertions.assertEquals(expectedTotal, pagination.total());
        Assertions.assertEquals(0, pagination.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_ShoulReturnPaginated(){
        final var expectedPage = 0 ;
        final var expectedPerPage = 1 ;
        final var expectedTotal = 3 ;

        final var filmes = Category.newCategory("Filme", null,true);
        final var series = Category.newCategory("Séries", null,true);
        final var documentarios = Category.newCategory("Documentário", null,true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJPAEntity.from(filmes),
                CategoryJPAEntity.from(series),
                CategoryJPAEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        //page 0
        CategorySearchQuery searchQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        Pagination<Category> pagination = categoryPostgresGateway.findAll(searchQuery);

        Assertions.assertEquals(expectedPage, pagination.currentPage());
        Assertions.assertEquals(expectedPerPage, pagination.perPage());
        Assertions.assertEquals(expectedTotal, pagination.total());
        Assertions.assertEquals(expectedPerPage, pagination.items().size());
        Assertions.assertEquals(documentarios.getName(), pagination.items().get(0).getName());

        //page1
        searchQuery = new CategorySearchQuery(1, 1, "", "name", "asc");
        pagination = categoryPostgresGateway.findAll(searchQuery);

        Assertions.assertEquals(1, pagination.currentPage());
        Assertions.assertEquals(expectedPerPage, pagination.perPage());
        Assertions.assertEquals(expectedTotal, pagination.total());
        Assertions.assertEquals(expectedPerPage, pagination.items().size());
        Assertions.assertEquals(filmes.getName(), pagination.items().get(0).getName());


        //page2
        searchQuery = new CategorySearchQuery(2, 1, "", "name", "asc");
        pagination = categoryPostgresGateway.findAll(searchQuery);

        Assertions.assertEquals(2, pagination.currentPage());
        Assertions.assertEquals(expectedPerPage, pagination.perPage());
        Assertions.assertEquals(expectedTotal, pagination.total());
        Assertions.assertEquals(expectedPerPage, pagination.items().size());
        Assertions.assertEquals(series.getName(), pagination.items().get(0).getName());
    }

    @Test
    public void givenAValidPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAnTermsMatchs_ShoulReturnPaginated(){
        final var expectedPage = 0 ;
        final var expectedPerPage = 1 ;
        final var expectedTotal = 1 ;

        final var filmes = Category.newCategory("Filme", null,true);
        final var series = Category.newCategory("Séries", null,true);
        final var documentarios = Category.newCategory("Documentário", null,true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJPAEntity.from(filmes),
                CategoryJPAEntity.from(series),
                CategoryJPAEntity.from(documentarios)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        CategorySearchQuery searchQuery = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        Pagination<Category> pagination = categoryPostgresGateway.findAll(searchQuery);

        Assertions.assertEquals(expectedPage, pagination.currentPage());
        Assertions.assertEquals(expectedPerPage, pagination.perPage());
        Assertions.assertEquals(expectedTotal, pagination.total());
        Assertions.assertEquals(expectedPerPage, pagination.items().size());
        Assertions.assertEquals(documentarios.getName(), pagination.items().get(0).getName());
    }
}
