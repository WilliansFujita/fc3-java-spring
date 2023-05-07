package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 255;
    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        final var name = this.category.getName();
        if(name == null)
            this.validationHandler().append(new Error("'Name' should not be null"));

        if(name != null && name.isBlank())
            this.validationHandler().append(new Error("'Name' should not be empty"));

        if(name != null){
            final var length = name.trim().length();
            if(length < MIN_LENGTH || length > MAX_LENGTH)
                this.validationHandler().append(new Error("'Name' must be betweeen 3 and 255 characters"));
        }

    }
}

