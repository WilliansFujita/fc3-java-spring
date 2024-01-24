package com.fullcycle.admin.catalogo.infrastructure.category.presenters;

import com.fullcycle.admin.catalogo.application.category.retrieve.get.CategoryOutPut;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;

public interface CategoryAPIPresenter {

    static CategoryAPIOutput present(final CategoryOutPut outPut){
        return new CategoryAPIOutput(
                outPut.id().getValue(),
                outPut.name(),
                outPut.description(),
                outPut.isActive(),
                outPut.createdAt(),
                outPut.updatedAt(),
                outPut.deletedAt()
        );
    }
}
