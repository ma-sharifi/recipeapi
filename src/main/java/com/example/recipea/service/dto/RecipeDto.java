package com.example.recipea.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahdi Sharifi
 * DTO for the Recipe .
 */

@Schema(description = "save Recipe Transfer data")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDto {

    private Long id;

    @Size(min = 2, max = 60, message = "#the size is important for this field")
    @NotNull(message = "#title is mandatory")
    private String title;

    private String username;
    @Size(min = 2, max = 2048, message = "#the size is important for this field")
    @NotNull(message = "#instruction can not be null")
    private String instruction;

    @NotNull(message = "#serve is mandatory")
    private Integer serve;

    private Boolean vegetarian;

    @JsonManagedReference
    private List<IngredientDto> ingredients = new ArrayList<>();

}
