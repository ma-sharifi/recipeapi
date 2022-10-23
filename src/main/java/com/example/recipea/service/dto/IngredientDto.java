package com.example.recipea.service.dto;

import com.example.recipea.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author Mahdi Sharifi
 * DTO for the Ingredient .
 */

@Schema(description = "save Ingredient Transfer data")
@Data @NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDto {

    @JsonProperty("id")
    private Long id;

    @NotNull(message = "#title is mandatory")
    @Size(min = 2, max = 60)
    private String title;

    @JsonBackReference //Circular References Handling
    private RecipeDto recipe;

    public IngredientDto(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "IngredientDto{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
