package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {
    private String name;

    private String description;

    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
                    final CategoryID anID,
                    final String aName,
                    final String aDescription,
                    final boolean isActive,
                    final Instant aCreationDate,
                    final Instant aUpdateDate,
                    final Instant aDeleteDate
        ) {
        super(anID);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDiscription, final boolean aActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        return new Category(id,aName,aDiscription,aActive,now,now,aActive? null: now);
    }

    public static Category with(
            final CategoryID anId,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.name,
                aCategory.description,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

    public CategoryID getId() {
        return id;
    }

    @Override
    public void validate(ValidationHandler validationHandler) {
        new CategoryValidator(this,validationHandler).validate();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Category deactivate() {
        if(getDeletedAt()==null){
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        this.active = true;
        this.updatedAt = Instant.now();
        this.deletedAt = null;
        return this;
    }

    public Category update(
            final String aName,
            final String aDescription,
            final boolean anActive) {
        this.name = aName;
        this.description = aDescription;
        if(anActive){
            activate();
        }else{
            deactivate();
        }

        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}