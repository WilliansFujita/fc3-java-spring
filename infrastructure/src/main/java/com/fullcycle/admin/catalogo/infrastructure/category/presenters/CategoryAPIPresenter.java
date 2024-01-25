package com.fullcycle.admin.catalogo.infrastructure.category.presenters;

import com.fullcycle.admin.catalogo.application.category.retrieve.get.CategoryOutPut;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.CategoryListOutPut;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryListResponse;

public interface CategoryAPIPresenter {

    static CategoryResponse present(final CategoryOutPut outPut){
        return new CategoryResponse(
                outPut.id().getValue(),
                outPut.name(),
                outPut.description(),
                outPut.isActive(),
                outPut.createdAt(),
                outPut.updatedAt(),
                outPut.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutPut outPut){
        return new CategoryListResponse(
                outPut.id().getValue(),
                outPut.name(),
                outPut.description(),
                outPut.isActive(),
                outPut.createdAt(),
                outPut.deletedAt()
        );
    }
}
