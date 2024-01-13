package br.com.bodegami.dscatalog.dto;

import br.com.bodegami.dscatalog.entities.Category;

public record CategoryDTO(
        Long id,
        String name
) {

    public CategoryDTO(Category entity) {
        this(entity.getId(), entity.getName());
    }

    public Category toModel() {
        return new Category(id, name);
    }

}
