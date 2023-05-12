package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.Error;

import java.util.function.Supplier;

public class DefaultGetCategoryByIDUseCase extends GetCategoryByIdUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIDUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CategoryOutPut execute(final String anIn) {

        final var aCategoryID = CategoryID.fromString(anIn);
        return this.categoryGateway.findById(aCategoryID)
                .map(CategoryOutPut::from)
                .orElseThrow(notFound(aCategoryID));
    }

    private Supplier<NotFoundException> notFound(CategoryID aCategoryID) {
        return ()-> NotFoundException.with(Category.class,aCategoryID);
    }
}
