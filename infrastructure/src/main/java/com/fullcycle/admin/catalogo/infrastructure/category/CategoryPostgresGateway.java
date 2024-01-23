package com.fullcycle.admin.catalogo.infrastructure.category;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

import static com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

@Service
public class CategoryPostgresGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryPostgresGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category aCategory) {
        return save(aCategory);
    }

    private Category save(Category aCategory) {
        return categoryRepository.save(CategoryJPAEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(CategoryID anID) {
        final String id = anID.getValue();
        if(categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID anID) {
        return this.categoryRepository.findById(anID.getValue()).map(CategoryJPAEntity::toAggregate);
    }

    @Override
    public Category update(Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perpage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()),aQuery.sort()));


        final  var specification = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str-> SpecificationUtils
                        .<CategoryJPAEntity>like("name", str)
                        .or(like("description",str)))
                .orElse(null);

        Page<CategoryJPAEntity> pageResult = categoryRepository.findAll(Specification.where(specification), page);

        return new Pagination<>(pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJPAEntity::toAggregate).toList());

    }
}
