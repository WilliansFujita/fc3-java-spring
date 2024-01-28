package com.fullcycle.admin.catalogo.e2e.category;

import com.fullcycle.admin.catalogo.E2ETest;
import org.testcontainers.containers.PostgisContainerProvider;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Container
    private static final PostgreSQLContainer  POSTGREE_CONTAINER =
            new PostgreSQLContainer("postgres:latest")
                    .withPassword("post-dev")
                    .withUsername("postgres")
                    .withDatabaseName("adm_videos");


    private static void setDataSourceProperties(){

    }
}
