package com.example.recipea.service.impl;

import com.example.recipea.entity.Recipe;
import com.example.recipea.exception.BadRequestException;
import com.example.recipea.exception.RecipeNotFoundException;
import com.example.recipea.repository.RecipeRepository;
import com.example.recipea.service.RecipeService;
import com.example.recipea.service.dto.CycleAvoidingMappingContext;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.mapper.RecipeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Mahdi Sharifi
 * Service  for  Recipe.
 */
@Transactional
@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    public static final String QUERY_INGREDIENT_IN_RECIPE = "SELECT * FROM T_RECIPE r " +
            "WHERE r.id ${NOT} in " +
            "(SELECT JND_RECIPE_INGREDIENT.RECIPE_ID from " +
            "JND_RECIPE_INGREDIENT  JOIN T_INGREDIENT i ON i.id = JND_RECIPE_INGREDIENT.INGREDIENT_ID " +
            "where 1<2 ${WHERE} ) AND 1<2 ";
    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    private final EntityManager em;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper, EntityManager em) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.em = em;
    }
    @Transactional(readOnly = true)
    public List<RecipeDto> findByIngredientsAndInstructionAndServeAndVegetarian(String ingredient,
                                                                                String instruction, Boolean isveg, Integer serve, String username) {

        log.debug(" #ingredients = " + ingredient + ", instruction = " + instruction + ", isveg = " + isveg + ", serve = " + serve);
        Set<String> ingredientsInclude = new HashSet<>();
        Set<String> ingredientsExclude = new HashSet<>();

        String[] ingredientsArray = null;
        if (ingredient != null) { // extract fields exluded and included and put them into set.
            ingredientsArray = ingredient.split(",");//potatoes, -salmon ,-diet
            ingredientsInclude = Arrays.stream(ingredientsArray).filter(i -> !i.startsWith("-")).map(String::trim).collect(Collectors.toSet());
            ingredientsExclude = Arrays.stream(ingredientsArray).filter(i -> ingredient.startsWith("-")).map(i -> ingredient.substring(1).trim()).collect(Collectors.toSet());
        }
        List<String> whereCause = new ArrayList<>();
        Map<Integer, Object> parameters = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(QUERY_INGREDIENT_IN_RECIPE);

        int counter = 1;
        String not = "";// I used it for excluding an ingredient like -salmon.

        //------ SELECT IN --- INGREDIENT -----------
        for (String include : ingredientsInclude) {
            if (!ingredient.isEmpty()) {
                parameters.put(counter, include.toLowerCase());
                whereCause.add(" and LOWER(i.title) = ?" + counter);
                counter++;
            }
        }
        for (String exclude : ingredientsExclude) {
            if (!ingredient.isEmpty()) {
                parameters.put(counter, exclude.toLowerCase());
                whereCause.add(" and LOWER(i.title) = ?" + counter);
                not = " not ";
                counter++;
            }
        }
        //----------- RECIPE -------
        List<String> whereCauseRecipe = new ArrayList<>();
        if (instruction != null && !instruction.isEmpty()) {
            parameters.put(counter, "%" + instruction.toLowerCase() + "%");
            whereCauseRecipe.add(" and LOWER(r.instruction) LIKE ?" + counter);
            counter++;
        }
        if (isveg != null) {
            parameters.put(counter, isveg);
            whereCauseRecipe.add(" and r.VEGETARIAN   = ?" + counter);
            counter++;
        }
        if (serve != null) {
            parameters.put(counter, serve);
            whereCauseRecipe.add(" and r.serve = ?" + counter);
            counter++;
        }
        if (username != null) {
            parameters.put(counter, username.toLowerCase());
            whereCauseRecipe.add(" and LOWER(r.username) = ?" + counter);
            counter++;
        }

        String queryNative = queryBuilder.append(StringUtils.join(whereCauseRecipe, " ")).toString().replace("${NOT}", not).replace("${WHERE}", StringUtils.join(whereCause, " "));
        log.debug("#queryNative: " + queryNative);
        Query jpaQuery = em.createNativeQuery(queryNative, Recipe.class);

        for (Map.Entry<Integer, Object> entry : parameters.entrySet()) {
            jpaQuery.setParameter(entry.getKey(), entry.getValue());
        }
        List<Recipe> result = (List<Recipe>) jpaQuery.getResultList();
        return result.stream().map(x -> recipeMapper.toDto(x, new CycleAvoidingMappingContext())).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeDto> findAll(Pageable pageable, String username) {
        return recipeRepository.findByUsername(pageable, username).map(recipe -> recipeMapper.toDto(recipe, new CycleAvoidingMappingContext()));
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeDto findOne(Long id, String name) throws RecipeNotFoundException {
        Optional<Recipe> accountOptional = recipeRepository.findByIdAndUsername(id, name);
        return accountOptional.map(recipe -> recipeMapper.toDto(recipe, new CycleAvoidingMappingContext())).orElseThrow(() ->
                new RecipeNotFoundException(id + ""));
    }

    @Override
    public RecipeDto save(RecipeDto recipeDto) {
        log.debug("#Save Recipe : {}", recipeDto);
        if (recipeDto.getId() != null) {
            throw new BadRequestException("A new recipe cannot already have an Id. expected null, but actual is: " + recipeDto.getId());
        }
        Recipe recipe = recipeMapper.toEntity(recipeDto, new CycleAvoidingMappingContext());
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe, new CycleAvoidingMappingContext());
    }

    @Override
    public RecipeDto update(@Valid @NotNull(message = "#recipeDto is mandatory") RecipeDto recipeDto) {
        log.debug("#Update Recipe: {}", recipeDto);
        if (recipeDto.getId() == null) {
            throw new BadRequestException("#Invalid Recipe id! For update uou need to provide id for the entity.");
        }
        Recipe recipe = recipeMapper.toEntity(recipeDto, new CycleAvoidingMappingContext());
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe, new CycleAvoidingMappingContext());
    }

    @Override
    public long deleteByIdAndUsername(Long id, String username) {
        log.debug("#Delete Recipe by id: " + id);
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isPresent()) {
            if (!username.equalsIgnoreCase(recipeOptional.get().getUsername()))
                throw new BadRequestException("id: " + id + " is not belong to user: " + username);
        } else throw new RecipeNotFoundException(id + "");
       return recipeRepository.deleteByIdAndUsername(id, username);
    }
}

