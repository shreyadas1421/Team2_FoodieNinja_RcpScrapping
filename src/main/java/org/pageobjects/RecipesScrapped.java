package org.pageobjects;

public class RecipesScrapped {
	
	private String recipeID,recipeName,ingredients,recipeDescription,preparationMethod,nutritionValues,recipeUrl,preperationTime,cookingTime,numOfServings,foodCategory,ingredientsName;
	
	private boolean lfvRecipesToAvoid;
	
	public String getIngredientsName() {
		return ingredientsName;
	}

	public void setIngredientsName(String ingredientsName) {
		this.ingredientsName = ingredientsName;
	}

	public String getFoodCategory() {
		return foodCategory;
	}

	public void setFoodCategory(String foodCategory) {
		this.foodCategory = foodCategory;
	}

	public String getPreperationTime() {
		return preperationTime;
	}

	public String getNumOfServings() {
		return numOfServings;
	}

	public void setNumOfServings(String numOfServings) {
		this.numOfServings = numOfServings;
	}

	public void setPreperationTime(String preperationTime) {
		this.preperationTime = preperationTime;
	}

	public String getCookingTime() {
		return cookingTime;
	}

	public void setCookingTime(String cookingTime) {
		this.cookingTime = cookingTime;
	}

	public String getRecipeDescription() {
		return recipeDescription;
	}

	public void setRecipeDescription(String recipeDescription) {
		this.recipeDescription = recipeDescription;
	}

	public String getPreparationMethod() {
		return preparationMethod;
	}

	public void setPreparationMethod(String preparationMethod) {
		this.preparationMethod = preparationMethod;
	}

	public String getNutritionValues() {
		return nutritionValues;
	}

	public void setNutritionValues(String nutritionValues) {
		this.nutritionValues = nutritionValues;
	}

	public String getRecipeUrl() {
		return recipeUrl;
	}

	public void setRecipeUrl(String recipeUrl) {
		this.recipeUrl = recipeUrl;
	}

	public String getRecipeID() {
		return recipeID;
	}

	public void setRecipeID(String recipeID) {
		this.recipeID = recipeID;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	
	public String toString() {
		return "ReceipeName :"+ recipeName + "Ingredenients :" + ingredients;
	}

	public boolean isLfvRecipesToAvoid() {
		return lfvRecipesToAvoid;
	}

	public void setLfvRecipesToAvoid(boolean lfvRecipesToAvoid) {
		this.lfvRecipesToAvoid = lfvRecipesToAvoid;
	}


}
