package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;

public record CreateCategoryOutput(String id) {

    public static CreateCategoryOutput from (final Category aCategory){
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from (final CategoryID anID){
        return new CreateCategoryOutput(anID.getValue());
    }

}
