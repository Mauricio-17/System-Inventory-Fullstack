package com.mauricio.inventory.integration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.mauricio.inventory.brand.Brand;
import com.mauricio.inventory.brand.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public
class BrandIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BrandRepository brandRepository;


    @Test
    void canRegisterNewBrand() throws Exception{
        // given
        Brand brand = new Brand();
        brand.setCountry("CANADA");
        brand.setName("STARLINK");
        //When
        ResultActions resultActions = mockMvc
                .perform(post("/api/brand")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header("Authorization", "Bearer <Put-the-proper-token>")
                                 .content(objectMapper.writeValueAsString(brand)));
        // then
        resultActions.andExpect(status().isCreated());
        List<Brand> brands = brandRepository.findAll();
        assertThat(brands)
                //.usingElementComparatorIgnoringFields("id") DEPRECATED method
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id") // we ignore ID
                // 'cause we don't have any control of it
                .contains(brand);

    }

    @Test
    void canDeleteBrand() throws Exception{

        //-----------------------------Given
        // Creating and adding a Brand
        Brand brand = new Brand();
        brand.setName("TELCEL");
        brand.setCountry("MEXICO");

        mockMvc.perform(post("/api/brand")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer <Put-the-proper-token>")
                                .content(objectMapper.writeValueAsString(brand)))
               .andExpect(status().isCreated());

        // Getting the current list of brands
        MvcResult getBrandsResult = mockMvc.perform(get("/api/brand")
                                                              .header("Authorization", "Bearer <Put-the-proper-token>")
                                                              .contentType(MediaType.APPLICATION_JSON))
                                             .andExpect(status().isOk())
                                             .andReturn();


        String contentAsString = getBrandsResult
                .getResponse()
                .getContentAsString();

        List<Brand> brands = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
                                                       );

        // Looking for the Brand created recently
        long id = brands.stream()
                          .filter(s -> s.getName().equals(brand.getName()))
                          .map(Brand::getId)
                          .findFirst()
                          .orElseThrow(() ->
                                               new IllegalStateException(
                                                       "Brand with name: " + brand.getName() + " "
                                                       + "not "
                                                       + "found"));
        // -------------------------------When
        ResultActions resultActions = mockMvc
                .perform(delete("/api/brand/"+id)
                                 .header("Authorization", "Bearer <Put-the-proper-token>")
                        );
        // -------------------------------Then
        resultActions.andExpect(status().isAccepted()); //We can ensure to expect the defined status
        boolean exists =  brandRepository.existsById(id);
        assertThat(exists).isFalse(); // We make sure that it's going to be false
        // for being able to pass the test
        //If anyone of them both tests doesn't fulfill, the test WON'T pass
    }

    @Test
    void canFetchBrands() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get("/api/brand")
                                 .header("Authorization", "Bearer <Put-the-proper-token>")
                        );
        resultActions.andExpect(status().isOk());
        List<Brand> brands = brandRepository.findAll();
        assertThat(brands).isNotEmpty();
    }

    @Test
    void canUpdateBrands() throws Exception{
        // Given
        Brand brand = new Brand();
        brand.setName("TELCEL");
        brand.setCountry("MEXICO");

        mockMvc.perform(post("/api/brand")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer <Put-the-proper-token>")
                                .content(objectMapper.writeValueAsString(brand)))
               .andExpect(status().isCreated());

        MvcResult getBrandsResult = mockMvc.perform(get("/api/brand")
                                                            .header("Authorization", "Bearer <Put-the-proper-token>")
                                                              .contentType(MediaType.APPLICATION_JSON))
                                             .andExpect(status().isOk())
                                             .andReturn();

        String contentAsString = getBrandsResult
                .getResponse()
                .getContentAsString();
        List<Brand> brands = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
                                                       );

        Long foundId = brands.stream()
                               .filter(s -> s.getName().equals(brand.getName()))
                               .map(Brand::getId)
                               .findFirst()
                               .orElseThrow(() ->
                                                    new IllegalStateException(
                                                            "Brand with name: " + brand.getName() + " "
                                                            + "not "
                                                            + "found"));

        Brand newBrand = new Brand();
        newBrand.setName("OAKLA");
        newBrand.setCountry("USA");

        mockMvc.perform(put("/api/brand/"+ foundId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer <Put-the-proper-token>")
                                .content(objectMapper.writeValueAsString(newBrand)))
               .andExpect(status().isAccepted());

        boolean existsName = brandRepository.existsName(newBrand.getName());
        assertThat(existsName).isTrue();
    }
}
