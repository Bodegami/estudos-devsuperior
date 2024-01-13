package br.com.bodegami.dscatalog.services;

import br.com.bodegami.dscatalog.dto.CategoryDTO;
import br.com.bodegami.dscatalog.entities.Category;
import br.com.bodegami.dscatalog.repositories.CategoryRepository;
import br.com.bodegami.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO request) {
        Category category = categoryRepository.save(request.toModel());
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO request) {
        try {
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(request.name());
            Category response = categoryRepository.save(entity);
            return new CategoryDTO(response);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Id not found: %d", id));
        }
    }

}
