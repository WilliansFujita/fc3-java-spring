package com.fullcycle.admin.catalogo.domain.category;

public record CategorySearchQuery(int page,
                                  int perpage,
                                  String terms,
                                  String sort,
                                  String direction) {
}
