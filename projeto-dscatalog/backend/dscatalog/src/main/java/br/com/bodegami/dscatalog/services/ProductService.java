package br.com.bodegami.dscatalog.services;

import br.com.bodegami.dscatalog.dto.ProductDTO;
import br.com.bodegami.dscatalog.entities.Product;
import br.com.bodegami.dscatalog.repositories.ProductRepository;
import br.com.bodegami.dscatalog.services.exceptions.DatabaseException;
import br.com.bodegami.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest)
                .map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource not found: %d", id)));
        return new ProductDTO(entity, entity.getCategories());
    }

//    @Transactional
//    public ProductDTO insert(ProductDTO request) {
//        Product product = productRepository.save(request.toModel());
//        return new ProductDTO(product);
//    }

//    @Transactional
//    public ProductDTO update(Long id, ProductDTO request) {
//        try {
//            Product entity = productRepository.getReferenceById(id);
//            entity.setName(request.name());
//            Product response = productRepository.save(entity);
//            return new ProductDTO(response);
//        }
//        catch (EntityNotFoundException e) {
//            throw new ResourceNotFoundException(String.format("Id not found: %d", id));
//        }
//    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

}
