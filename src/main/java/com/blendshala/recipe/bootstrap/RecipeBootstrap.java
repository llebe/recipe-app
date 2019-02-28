package com.blendshala.recipe.bootstrap;

import com.blendshala.recipe.domain.*;
import com.blendshala.recipe.repositories.CategoryRepository;
import com.blendshala.recipe.repositories.RecipeRepository;
import com.blendshala.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Loading Bootstrap Data");
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>(2);

        //get UOMs
        Optional<UnitOfMeasure> eachUomOptional = unitOfMeasureRepository.findByDescription("Each");

        if(!eachUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM each not found");
        }

        Optional<UnitOfMeasure> tableSpoonUomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");

        if(!tableSpoonUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM table not found");
        }

        Optional<UnitOfMeasure> teaSpoonOptional = unitOfMeasureRepository.findByDescription("Teaspoon");

        if(!teaSpoonOptional.isPresent()){
            throw new RuntimeException("Expected UOM tea not found");
        }

        Optional<UnitOfMeasure> dashUomOptional = unitOfMeasureRepository.findByDescription("Dash");

        if(!dashUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM dash not found");
        }

        Optional<UnitOfMeasure> pintUomOptional = unitOfMeasureRepository.findByDescription("Pint");

        if(!pintUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM pint not found");
        }

        Optional<UnitOfMeasure> cupsUomOptional = unitOfMeasureRepository.findByDescription("Cups");

        if(!cupsUomOptional.isPresent()){
            throw new RuntimeException("Expected UOM cups not found");
        }

        //get optionals
        UnitOfMeasure eachUom = eachUomOptional.get();
        UnitOfMeasure tableSpoonUom = tableSpoonUomOptional.get();
        UnitOfMeasure teaSpoonUom = teaSpoonOptional.get();
        UnitOfMeasure dashUom = dashUomOptional.get();
        UnitOfMeasure pintUom = pintUomOptional.get();
        UnitOfMeasure cupsUom = cupsUomOptional.get();

        //get Categories
        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");

        if(!americanCategoryOptional.isPresent()) {
            throw new RuntimeException("Expected Category Not Found");
        }

        Optional<Category> mexicananCategoryOptional = categoryRepository.findByDescription("Mexican");

        if(!mexicananCategoryOptional.isPresent()) {
            throw new RuntimeException("Expected Category Not Found");
        }

        Category americanCategory = americanCategoryOptional.get();
        Category mexicanCategory = mexicananCategoryOptional.get();

        //Yummy Guac
        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl." +
                "\n" + "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)" +
                "\n" + "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown." +
                "\n" + "Read more: https://www.simplyrecipes.com/recipes/perfect_guacamole/");


        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("Be careful handling chiles if using. Wash your hands thoroughly after handling and do not touch your eyes or the area near your eyes with your hands for several hours.");
        guacNotes.setRecipe(guacRecipe);
        guacRecipe.setNotes(guacNotes);


        guacRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), eachUom));
        guacRecipe.addIngredient(new Ingredient("Kusher Salt", new BigDecimal(.5), teaSpoonUom));
        guacRecipe.addIngredient(new Ingredient("Lemon juice", new BigDecimal(2), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("Red onion", new BigDecimal(2), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("Chiles", new BigDecimal(2), eachUom));
        guacRecipe.addIngredient(new Ingredient("Cilantro", new BigDecimal(2), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("Black Pepper", new BigDecimal(2), dashUom));
        guacRecipe.addIngredient(new Ingredient("ripe tomato", new BigDecimal(.5), eachUom));

        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);

        recipes.add(guacRecipe);



        return recipes;
    }
}
