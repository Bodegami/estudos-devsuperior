package br.com.bodegami.dscatalog.services;

import br.com.bodegami.dscatalog.dto.ProductDTO;
import br.com.bodegami.dscatalog.entities.Product;
import br.com.bodegami.dscatalog.repositories.ProductRepository;
import br.com.bodegami.dscatalog.services.exceptions.DatabaseException;
import br.com.bodegami.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.bodegami.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;


    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));



        when(productRepository.save(any())).thenReturn(product);

        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

    }


    @Test
    public void findAllPagedShouldReturnPage() {

        when(productRepository.findAll((Pageable) any())).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertNotNull(result);

        verify(productRepository, times(1)).findAll((Pageable) any());
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        when(productRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(existingId);

        assertDoesNotThrow(() -> service.delete(existingId));

        verify(productRepository, times(1)).existsById(existingId);
        verify(productRepository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        when(productRepository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
             service.delete(nonExistingId);
        });

        verify(productRepository, times(1)).existsById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId() {

        when(productRepository.existsById(dependentId)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

        verify(productRepository, times(1)).existsById(dependentId);
        verify(productRepository, times(1)).deleteById(dependentId);
    }

}