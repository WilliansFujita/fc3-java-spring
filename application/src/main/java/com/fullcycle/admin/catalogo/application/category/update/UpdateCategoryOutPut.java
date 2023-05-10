package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutPut(
        CategoryID id
) {

    public static UpdateCategoryOutPut from (Category aCategory){
        return new UpdateCategoryOutPut(aCategory.getId());
    }
}
