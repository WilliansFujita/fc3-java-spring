package com.fullcycle.admin.catalogo.infrastructure.category;

import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryPostgresGatewayTest {

    @Autowired
    private CategoryPostgresGateway categoryPostgresGateway;

    @Autowired
    private CategoryRepository categoryRepository;
}
