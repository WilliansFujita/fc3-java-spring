package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryAPIInput;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryAPIInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(value = "/categories")
public interface CategoryAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "Unprocessable error"),
    })
    ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryAPIInput input);

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "Unprocessable error"),
            @ApiResponse(responseCode = "500", description = "an internal server error was thrown"),
    })
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final String perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String asc
    );

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "404", description = "was not found"),
            @ApiResponse(responseCode = "500", description = "an internal server error was thrown"),
    })
    CategoryAPIOutput getByID(@PathVariable(name = "id", required = true) final String id);

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated succssfully"),
            @ApiResponse(responseCode = "404", description = "was not found"),
            @ApiResponse(responseCode = "500", description = "an internal server error was thrown"),
    })
    ResponseEntity<?> updateByID(@PathVariable(name = "id", required = true) final String id, @RequestBody @Valid UpdateCategoryAPIInput input);

    @DeleteMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Delete a category by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted succssfully"),
            @ApiResponse(responseCode = "404", description = "was not found"),
            @ApiResponse(responseCode = "500", description = "an internal server error was thrown"),
    })
    void deleteByID(@PathVariable(name = "id", required = true) final String id);
}
