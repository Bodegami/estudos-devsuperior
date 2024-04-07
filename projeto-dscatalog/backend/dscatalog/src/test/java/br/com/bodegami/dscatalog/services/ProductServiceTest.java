package br.com.bodegami.dscatalog.services;

import br.com.bodegami.dscatalog.repositories.ProductRepository;
import br.com.bodegami.dscatalog.services.exceptions.DatabaseException;
import br.com.bodegami.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
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



    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 3L;

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