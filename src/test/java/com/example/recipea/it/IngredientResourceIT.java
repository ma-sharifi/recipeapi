//package com.example.recipea.it;
//
//
//import com.example.recipea.TestUtil;
//import com.example.recipea.entity.Ingredient;
//import com.example.recipea.repository.IngredientRepository;
//import com.example.recipea.service.dto.IngredientDto;
//import com.example.recipea.service.mapper.IngredientMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Integration tests for the IngredientResource REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class IngredientResourceIT {
//
//    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
//    private static final String UPDATED_TITLE = "BBBBBBBBBB";
//
//    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
//    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);
//
//    private static final String ENTITY_API_URL = "/api/ingredients";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private IngredientRepository ingredientRepository;
//
//    @Autowired
//    private IngredientMapper ingredientMapper;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restIngredientMockMvc;
//
//    private Ingredient ingredient;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Ingredient createEntity(EntityManager em) {
//        Ingredient ingredient = new Ingredient().title(DEFAULT_TITLE).createdAt(DEFAULT_CREATED_AT);
//        return ingredient;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Ingredient createUpdatedEntity(EntityManager em) {
//        Ingredient ingredient = new Ingredient().title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT);
//        return ingredient;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        ingredient = createEntity(em);
//    }
//
////    @Test
////    @Transactional
////    void createIngredient() throws Exception {
////        int databaseSizeBeforeCreate = ingredientRepository.findAll().size();
////        // Create the Ingredient
////        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
////        restIngredientMockMvc
////            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
////            .andExpect(status().isCreated());
////
////        // Validate the Ingredient in the database
////        List<Ingredient> ingredientList = ingredientRepository.findAll();
////        assertThat(ingredientList).hasSize(databaseSizeBeforeCreate + 1);
////        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
////        assertThat(testIngredient.getTitle()).isEqualTo(DEFAULT_TITLE);
////    }
//
//    @Test
//    @Transactional
//    void createIngredientWithExistingId() throws Exception {
//        // Create the Ingredient with an existing ID
//        ingredient.setId(1L);
//        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
//
//        int databaseSizeBeforeCreate = ingredientRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restIngredientMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void checkTitleIsRequired() throws Exception {
//        int databaseSizeBeforeTest = ingredientRepository.findAll().size();
//        // set the field null
//        ingredient.setTitle(null);
//
//        // Create the Ingredient, which fails.
//        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
//
//        restIngredientMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
//            .andExpect(status().isBadRequest());
//
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void getAllIngredients() throws Exception {
//        // Initialize the database
//        ingredientRepository.saveAndFlush(ingredient);
//
//        // Get all the ingredientList
//        restIngredientMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredient.getId().intValue())))
//            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
//            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
//    }
//
//    @Test
//    @Transactional
//    void getIngredient() throws Exception {
//        // Initialize the database
//        ingredientRepository.saveAndFlush(ingredient);
//
//        // Get the ingredient
//        restIngredientMockMvc
//            .perform(get(ENTITY_API_URL_ID, ingredient.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(ingredient.getId().intValue()))
//            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
//            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingIngredient() throws Exception {
//        // Get the ingredient
//        restIngredientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewIngredient() throws Exception {
//        // Initialize the database
//        ingredientRepository.saveAndFlush(ingredient);
//
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//
//        // Update the ingredient
//        Ingredient updatedIngredient = ingredientRepository.findById(ingredient.getId()).get();
//        // Disconnect from session so that the updates on updatedIngredient are not directly saved in db
//        em.detach(updatedIngredient);
//        updatedIngredient.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT);
//        IngredientDto ingredientDTO = ingredientMapper.toDto(updatedIngredient);
//
//        restIngredientMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, ingredientDto.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
//        assertThat(testIngredient.getTitle()).isEqualTo(UPDATED_TITLE);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingIngredient() throws Exception {
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//        ingredient.setId(count.incrementAndGet());
//
//        // Create the Ingredient
//        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restIngredientMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, ingredientDTO.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchIngredient() throws Exception {
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//        ingredient.setId(count.incrementAndGet());
//
//        // Create the Ingredient
//        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restIngredientMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamIngredient() throws Exception {
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//        ingredient.setId(count.incrementAndGet());
//
//        // Create the Ingredient
//        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restIngredientMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredientDTO)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateIngredientWithPatch() throws Exception {
//        // Initialize the database
//        ingredientRepository.saveAndFlush(ingredient);
//
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//
//        // Update the ingredient using partial update
//        Ingredient partialUpdatedIngredient = new Ingredient();
//        partialUpdatedIngredient.setId(ingredient.getId());
//
//        partialUpdatedIngredient.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT);
//
//        restIngredientMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedIngredient.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredient))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
//        assertThat(testIngredient.getTitle()).isEqualTo(UPDATED_TITLE);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateIngredientWithPatch() throws Exception {
//        // Initialize the database
//        ingredientRepository.saveAndFlush(ingredient);
//
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//
//        // Update the ingredient using partial update
//        Ingredient partialUpdatedIngredient = new Ingredient();
//        partialUpdatedIngredient.setId(ingredient.getId());
//
//        partialUpdatedIngredient.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT);
//
//        restIngredientMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedIngredient.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredient))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
//        assertThat(testIngredient.getTitle()).isEqualTo(UPDATED_TITLE);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingIngredient() throws Exception {
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//        ingredient.setId(count.incrementAndGet());
//
//        // Create the Ingredient
//        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restIngredientMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, ingredientDTO.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchIngredient() throws Exception {
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//        ingredient.setId(count.incrementAndGet());
//
//        // Create the Ingredient
//        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restIngredientMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamIngredient() throws Exception {
//        int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();
//        ingredient.setId(count.incrementAndGet());
//
//        // Create the Ingredient
//        IngredientDto ingredientDTO = ingredientMapper.toDto(ingredient);
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restIngredientMockMvc
//            .perform(
//                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ingredientDTO))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the Ingredient in the database
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteIngredient() throws Exception {
//        // Initialize the database
//        ingredientRepository.saveAndFlush(ingredient);
//
//        int databaseSizeBeforeDelete = ingredientRepository.findAll().size();
//
//        // Delete the ingredient
//        restIngredientMockMvc
//            .perform(delete(ENTITY_API_URL_ID, ingredient.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
