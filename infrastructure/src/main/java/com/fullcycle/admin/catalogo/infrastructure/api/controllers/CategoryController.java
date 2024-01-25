package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryOutPut;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.api.CategoryAPI;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.presenters.CategoryAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private CreateCategoryUseCase createCategoryUseCase;

    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    private UpdateCategoryUseCase updateCategoryUseCase;

    private DeleteCategoryUseCase deleteCategoryUseCase;

    private ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryByIdUseCase getCategoryByIdUseCase, UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest input) {

        final var command = CreateCategoryCommand
                .with(
                        input.name(),
                        input.description(),
                        input.active() != null ? input.active() : true
                );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/"+ output.id())).body(output);

        return this.createCategoryUseCase.execute(command)
                .fold(onError,onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(String search, int page, int perPage, String sort, String direction) {

        final var searchParams = new CategorySearchQuery(page, perPage,search,sort,direction);

        return listCategoriesUseCase.execute(searchParams)
                .map(CategoryAPIPresenter::present);

    }

    @Override
    public CategoryResponse getByID(final String id) {
        return CategoryAPIPresenter.present(getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateByID(String id, UpdateCategoryRequest input) {

        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification,ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutPut, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.ok(output);

        return updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);


    }

    @Override
    public void deleteByID(final String id) {

        deleteCategoryUseCase
                .execute(id);


    }


}
