package br.com.bodegami.dscatalog.services;

import br.com.bodegami.dscatalog.entities.Category;
import br.com.bodegami.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(null, "Books"));
        categories.add(new Category(null, "Eletronics"));

        categoryRepository.saveAll(categories);

        return categoryRepository.findAll();
    }

}
