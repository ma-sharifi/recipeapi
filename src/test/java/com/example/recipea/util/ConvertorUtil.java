package com.example.recipea.util;

import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * @author Mahdi Sharifi
 */
public enum ConvertorUtil {
    INSTANCE;
    public static ResponseDto<RecipeDto> toResponseDto(String jsonStrings)  {
        Type collectionType = new TypeToken<ResponseDto<RecipeDto>>() {
        }.getType(); //because ResponseDto has a generic type we need defied TypeToken for it.
        Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
        ResponseDto<RecipeDto> responseDto = GSON.fromJson(jsonStrings, collectionType);
        return responseDto;
    }
}
