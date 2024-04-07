package br.com.bodegami.dscatalog.controllers;

import br.com.bodegami.dscatalog.dto.ProductDTO;
import br.com.bodegami.dscatalog.services.ProductService;
import br.com.bodegami.dscatalog.services.exceptions.DatabaseException;
import br.com.bodegami.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.bodegami.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper mapper;
    private ProductDTO productDTO;

    private PageImpl<ProductDTO> page;
    private long existingId;
    private long nonExistingId;
    private long dependentId;


    @BeforeEach
    void setUp() {
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        nonExistingId = 100L;
        dependentId = 3L;
    }

    @Test
    public void findAllPagedShouldReturnPage() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);

        when(service.findAllPaged(pageable)).thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @Test
    public void insertShouldReturnProduct() throws Exception {

        when(service.insert(any())).thenReturn(productDTO);

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }


    @Test
    public void deleteShouldReturnNothingWhenIdExists() throws Exception {

        doNothing().when(service).delete(existingId);

        ResultActions result = mockMvc
                .perform(delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnResourceDatabaseExceptionWhenDependentId() throws Exception {

        doThrow(DatabaseException.class).when(service).delete(dependentId);

        ResultActions result = mockMvc
                .perform(delete("/products/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteShouldReturnResourceNotFoundWhenIdDoesNotExist() throws Exception {

        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);

        ResultActions result = mockMvc
                .perform(delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductWhenIdExists() throws Exception {

        when(service.update(eq(existingId), any())).thenReturn(productDTO);

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {

        when(service.findById(existingId)).thenReturn(productDTO);

        ResultActions result = mockMvc
                .perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        ResultActions result = mockMvc
                .perform(get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

}