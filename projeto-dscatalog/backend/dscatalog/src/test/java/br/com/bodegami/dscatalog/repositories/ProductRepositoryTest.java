package br.com.bodegami.dscatalog.repositories;

import br.com.bodegami.dscatalog.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        //ACT
        long existingId = 1L;

        //ARRANGE
        productRepository.deleteById(existingId);

        Optional<Product> result = productRepository.findById(existingId);

        //ASSERT
        assertTrue(result.isEmpty());
    }

}