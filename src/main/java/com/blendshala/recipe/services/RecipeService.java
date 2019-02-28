package com.blendshala.recipe.services;

import com.blendshala.recipe.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
}
