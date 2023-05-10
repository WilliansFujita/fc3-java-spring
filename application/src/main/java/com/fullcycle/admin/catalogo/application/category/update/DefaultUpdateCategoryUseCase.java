package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutPut> execute(UpdateCategoryCommand aCommand) {
        final var anId = CategoryID.fromString(aCommand.id());
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = this.categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();

        aCategory
                .update(aName,aDescription,isActive)
                .validate(notification);

        return notification.hasError()? Left(notification): update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutPut> update(Category aCategory) {
        return Try(()->categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create,UpdateCategoryOutPut::from);
    }

    private static Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(new Error("Category ID %d was not found".formatted(anId)));
    }
}
