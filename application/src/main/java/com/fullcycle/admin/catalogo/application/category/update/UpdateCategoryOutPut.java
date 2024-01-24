package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutPut(
        String id
) {

    public static UpdateCategoryOutPut from (Category aCategory){
        return new UpdateCategoryOutPut(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutPut from(final String id) {
        return new UpdateCategoryOutPut(id);
    }
}
