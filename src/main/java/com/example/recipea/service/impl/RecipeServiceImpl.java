package com.example.recipea.service.impl;

import com.example.recipea.exception.BadRequestException;
import com.example.recipea.exception.RecipeNotFoundException;
import com.example.recipea.service.dto.CycleAvoidingMappingContext;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.entity.Recipe;
import com.example.recipea.service.dto.ResponseDto;
import com.example.recipea.service.mapper.IngredientMapper;
import com.example.recipea.service.mapper.RecipeMapper;
import com.example.recipea.repository.RecipeRepository;
import com.example.recipea.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    private EntityManager em;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper, EntityManager em) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.em=em;
    }

    public List<RecipeDto> findByIngredientsAndInstructionAndServeAndVegetarian(String ingredient,
                                                                      String instruction, Boolean isveg, Integer serve) {

        log.debug(" #ingredients = " + ingredient + ", instruction = " + instruction + ", isveg = " + isveg + ", serve = " + serve);
        Set<String> ingredientsInclude=new HashSet<>();
        Set<String> ingredientsExclude = new HashSet<>();

        String[] ingredientsArray=null;
         if(ingredient!=null) {
             ingredientsArray = ingredient.split(",");//Gluten Free, Vegetarian,-Diet
             ingredientsInclude = Arrays.stream(ingredientsArray).filter(i -> !i.startsWith("-")).map(String::trim).collect(Collectors.toSet());
             ingredientsExclude = Arrays.stream(ingredientsArray).filter(i -> ingredient.startsWith("-")).map(i -> ingredient.substring(1).trim()).collect(Collectors.toSet());
         }

        log.debug("#ingredientsArray-> " + ingredientsArray);
        log.debug("#ingredientsInclude-> " + ingredientsInclude);
        log.debug("#ingredientsExclude-> " + ingredientsExclude);
        //https://www.baeldung.com/spring-data-criteria-queries
        List<String> whereCause = new ArrayList<>();
        Map<Integer, Object> parameters = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("select * from T_RECIPE r " +
                "where r.id ${NOT} in " +
                "(select JND_RECIPE_INGREDIENT.RECIPE_ID from " +
                "JND_RECIPE_INGREDIENT  join T_INGREDIENT i on i.id = JND_RECIPE_INGREDIENT.INGREDIENT_ID " +
                "where 1<2 ${WHERE} ) AND 1<2 ");

        int counter = 1;
        String not = "";

        //------ SELECT IN ------ INGREDIENT -----------
        for (String include : ingredientsInclude) {
            if (!ingredient.isEmpty()) {
                parameters.put(counter, include.toUpperCase());
                whereCause.add(" and UPPER(i.title) = ?" + counter);
                counter++;
            }
        }
        for (String exclude : ingredientsExclude) {
            if (!ingredient.isEmpty()) {
                parameters.put(counter, exclude.toUpperCase());
                whereCause.add(" and UPPER(i.title) = ?" + counter);
                not = " not ";
                counter++;
            }
        }
        //----------- RECIPE -------
        List<String> whereCauseRecipe = new ArrayList<>();
        if (instruction != null && !instruction.isEmpty()) {
            parameters.put(counter, "%" + instruction.toUpperCase() + "%");
            whereCauseRecipe.add(" and UPPER(r.instruction) LIKE ?" + counter);
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

        String queryNative = queryBuilder.append(StringUtils.join(whereCauseRecipe, " ")).toString().replace("${NOT}", not).replace("${WHERE}", StringUtils.join(whereCause, " "));
        log.debug("#queryNative: "+queryNative);
        Query jpaQuery = em.createNativeQuery(queryNative, Recipe.class);

        for (Map.Entry<Integer, Object> entry: parameters.entrySet()) {
            jpaQuery.setParameter(entry.getKey(), entry.getValue());
        }
        List<Recipe> result = (List<Recipe>) jpaQuery.getResultList();
        return result.stream().map(x -> recipeMapper.toDto(x, new CycleAvoidingMappingContext())).collect(Collectors.toList());
    }

    @Override
    public Page<RecipeDto> findAll(Pageable pageable) {
        return recipeRepository.findAll(pageable).map(recipe -> recipeMapper.toDto(recipe,new CycleAvoidingMappingContext()));
    }

    @Override
    public RecipeDto findOne(Long id) throws RecipeNotFoundException {
        Optional<Recipe> accountOptional = recipeRepository.findById(id);
        return accountOptional.map(recipe -> recipeMapper.toDto(recipe,new CycleAvoidingMappingContext())).orElseThrow(() ->
                new RecipeNotFoundException(id+""));
    }



    @Override
    public RecipeDto save(RecipeDto recipeDto) {
        log.debug("#Save Recipe : {}", recipeDto);
        if (recipeDto.getId() != null) {
            throw new BadRequestException("A new recipe cannot already have an Id. expected null, but actual is: "+recipeDto.getId() );
        }
        Recipe recipe = recipeMapper.toEntity(recipeDto,new CycleAvoidingMappingContext());
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe , new CycleAvoidingMappingContext());
    }

    @Override
    public RecipeDto update(@Valid @NotNull(message ="#recipeDto is mandatory") RecipeDto recipeDto) {
        log.debug("#Update Recipe: {}", recipeDto);
        if (recipeDto.getId() == null) {
            throw new BadRequestException("#Invalid Recipe id! For update uou need to provide id for the entity.");
        }

        Recipe recipe = recipeMapper.toEntity(recipeDto , new CycleAvoidingMappingContext());
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(recipe , new CycleAvoidingMappingContext());
    }

    @Override
    public void delete(Long id) {
        log.debug("#Delete Recipe by id: "+ id);
        recipeRepository.deleteById(id);
    }
}

