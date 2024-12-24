package org.pageobjects;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import base.*;
import utils.*;

public class newurl extends TestBase{
	
	List<RecipesScrapped> allRecipesList = new ArrayList<RecipesScrapped>();
	List<RecipesScrapped> lchfAddRecipes = new ArrayList<RecipesScrapped>();
	List<RecipesScrapped> lchfEliminationRecipes = new ArrayList<RecipesScrapped>();
	List<RecipesScrapped> lfvEliminationRecipes = new ArrayList<RecipesScrapped>();
	List<RecipesScrapped> lfvAddRecipes = new ArrayList<RecipesScrapped>();
	List<RecipesScrapped> lfvToAddRecipes = new ArrayList<RecipesScrapped>();
	List<RecipesScrapped> lfvToAddEliminationRecipes = new ArrayList<RecipesScrapped>();
	List<RecipesScrapped> lfvNutAllergyEliminationRecipes=new ArrayList<RecipesScrapped>();
	List<RecipesScrapped> lfvOtherAllergyEliminationRecipes=new ArrayList<RecipesScrapped>();
	
	DatabaseClass db;
	 
	public newurl(WebDriver driver) {
		System.out.println("Hi");
		PageFactory.initElements(driver,this);
	}
	
	public void click_AtoZ_recipes()	{
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.xpath("//a[@href='recipes-for-cuisine-1' and @class='dropdown-toggle']")).click();
		driver.findElement(By.xpath("//a[@href='https://www.tarladalal.com/recipes-for-indian-veg-recipes-2']")).click();
		driver.findElement(By.xpath("//div[@id='toplinks']/a[text()='Recipe A To Z']")).click();
		System.out.println("A to Z is clicked..");
	}
	
	
	public void jgetRecipieInfo() throws Exception {
		
		
		int exp=0;
		int pageCount;
		
		db = new DatabaseClass();
		db.connect();
		db.createTableAllRecipies();
		db.createTableLFVEliminate();
		
		for(int i=26; i<=26; i++) 
		{
			//Find the alphabet	and click on the alphabet link		
			WebElement AlphabetLink=driver.findElement(By.xpath("//table[@id='ctl00_cntleftpanel_mnuAlphabets']/tbody/tr/td[@id='ctl00_cntleftpanel_mnuAlphabetsn"+i+"']//a"));
			String alphabet=AlphabetLink.getText();
			AlphabetLink.click();
			
			if(alphabet.equals("X")) {
				pageCount=0;
			}		
			else 
			  {
				WebElement divElement = driver.findElement(By.xpath("//div[@style='text-align:right;padding-bottom:15px;']"));
				List<WebElement> anchorTags = divElement.findElements(By.tagName("a"));
				pageCount = anchorTags.size();
				System.out.println("Toal page count is: "+pageCount);
			  }
				
			for(int pg=1; pg<=pageCount; pg++) 
			 {
				try 
				 {
					WebElement current_pg=driver.findElement(By.xpath("//div[@style='text-align:right;padding-bottom:15px;'][1]/a[text()='"+pg+"']"));
					current_pg.click();
					System.out.println("******  Alphabet is "+alphabet+"  **** Current page is: "+pg+"  *********");
				 }
				
				catch(Exception e) 
				{	
					System.out.println("%%%%% Exception Pages %%%%%% " +exp++);
				}
				
				//Get the list of recipe's in the page				
				List<WebElement> recipes_url=driver.findElements(By.className("rcc_recipename"));
				//int no_of_cards=recipes_url.size();
				ArrayList<String> links=new ArrayList<>(30);
				String recipesLink="";
				String rl;
				for(WebElement url: recipes_url) 
				 {
					recipesLink=url.findElement(By.tagName("a")).getAttribute("href");
					links.add(recipesLink);
					((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", recipesLink);
			     }
				for (int j = 0; j < links.size(); j++) {
					 rl = links.get(j);
		            System.out.println("Recipie no: "+(j+1)+" Recipe Link: " + rl);
		        }
				
				int r=1;
				ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
				String parentTab = driver.getWindowHandle();
				for (String tab : tabs) {
					
					if (!tab.equals(parentTab)) {
						driver.switchTo().window(tab);
						try {
							System.out.println("Alphabet: "+alphabet+"------Page:"+pg+"-------Scrapping Recipie no: "+r+" "+links.get(r-1)+"--------");
							recipeDataScraper(driver);
							r++;
							
						} catch (Exception e) {
							System.out.println("Error scraping data from tab: " + tab);
							r++;
						}
						driver.close(); // Close the current tab
					}
				}
				driver.switchTo().window(parentTab); // Switch back to the parent tab
	
							
			 }//For loop for pages
	
		}//For loop of the alphabets
		
		/********************         Filter LFV & LCH -To eliminate, To add,      **************************/
		
		System.out.println("@@@@@@@@@     Total number of recipies scrapped :"+allRecipesList.size()+"     @@@@@@@@@@@@@@@@@@@@@@");
		for (int i = 0; i < allRecipesList.size(); i++) {
            RecipesScrapped rlist = allRecipesList.get(i);
            System.out.println("Recipe_ID: " + rlist.getRecipeID() + ", Recipe_Name: " + rlist.getRecipeName()+ ", Recipe_Category: "+ rlist.getFoodCategory()
                                +"Food_Category: " + rlist.getFoodCategory() + ", Ingredie nts: " + rlist.getIngredientsName()+ ", Preparation_Time: "+ rlist.getPreperationTime()
                               );
            db.insertRecipeDataAllRecipies(rlist.getRecipeID(), rlist.getRecipeName(),rlist.getFoodCategory(),rlist.getIngredientsName(), 
            		rlist.getPreperationTime(),rlist.getCookingTime(),rlist.getNumOfServings(),
            		rlist.getRecipeDescription(),rlist.getPreparationMethod(),rlist.getNutritionValues(),rlist.getRecipeUrl());
        }
		
		System.out.println("@@@@@@@@@  LFV ELIMINATE  @@@@@@@@@@@@@@");
		lfvEliminationRecipes = filterRecipes(allRecipesList, TestUtil.LFV_TO_ELIMINATE, true);
		System.out.println("******  Total no. of recipies after lfvElimination  : " + lfvEliminationRecipes.size());
		for (int i = 0; i < lfvEliminationRecipes.size(); i++) {
            RecipesScrapped rlist = lfvEliminationRecipes.get(i);
            System.out.println("Recipe_ID: " + rlist.getRecipeID() + ", Recipe_Name: " + rlist.getRecipeName()+ ", Recipe_Category: "+ rlist.getFoodCategory()
                                +"Food_Category: " + rlist.getFoodCategory() + ", Ingredients: " + rlist.getIngredientsName()+ ", Preparation_Time: "+ rlist.getPreperationTime()
                               );
            db.insertRecipeDataLFVEliminate(rlist.getRecipeID(), rlist.getRecipeName(),rlist.getFoodCategory(),rlist.getIngredientsName(), 
          		rlist.getPreperationTime(),rlist.getCookingTime(),rlist.getNumOfServings(),
            		rlist.getRecipeDescription(),rlist.getPreparationMethod(),rlist.getNutritionValues(),rlist.getRecipeUrl());
        }
				
		System.out.println("@@@@@@@@@  LCHF ELIMINATE  @@@@@@@@@@@@@@");
		lchfEliminationRecipes = filterRecipes(allRecipesList, TestUtil.LCHF_TO_ELIMINATE, true);
		System.out.println("******  Total no. of recipies after lchfElimination  : " + lchfEliminationRecipes.size());
		System.out.println("******Recipie List after lchfElimination : " + lchfEliminationRecipes);
		
		
		System.out.println("@@@@@@@@@  LFV ADD  @@@@@@@@@@@@@@");
		lfvAddRecipes = filterRecipes(lfvEliminationRecipes, TestUtil.LFV_ADD, false);
		System.out.println("****** Total no. of recipies for lfvAdd  : " + lfvAddRecipes.size());
		System.out.println("******Recipie List after lfvAdd : " + lfvAddRecipes);
	
		System.out.println("@@@@@@@@@  LCHF ADD  @@@@@@@@@@@@@@");
		lchfAddRecipes = filterRecipes(lchfEliminationRecipes, TestUtil.LCHF_ADD, false);
		System.out.println("******  Total no. of recipies lchfAdd  : " + lchfAddRecipes.size());
		System.out.println("******Recipie List after lchfElimination : " + lchfAddRecipes);


		//To get partial vegan recipes including LFV to Add recipes, we are adding lfv to add elimination recipes(butter included)and lfv to add
		lfvToAddEliminationRecipes = filterRecipes(allRecipesList, TestUtil.LFV_TO_ELIMINATE_NFV, true);//here we get recipe list after elimination inlcuding butter
		System.out.println("******lfvToAddEliminationRecipes RECIPES : " + lfvToAddEliminationRecipes);
		lfvToAddRecipes = filterRecipes(lfvToAddEliminationRecipes, TestUtil.LFV_TO_ADD, false);//filtering recipes with butter with add ingredients
		System.out.println("******lfvToAddRecipes RECIPES : " + lfvToAddRecipes);
		lfvToAddRecipes.addAll(lfvAddRecipes);//adding "lfv to add recipes" with "lfv add recipes" to get partial vegan LFV recipes
		System.out.println("******PartialVeganLFV RECIPES : " + lfvToAddRecipes);
		
		//to eliminate Allergy Nut Recipes from LFV To Eliminate Recipes		
		lfvNutAllergyEliminationRecipes=filterRecipes(lfvToAddEliminationRecipes,TestUtil.LFV_NUT_ALLERGY,true);
		System.out.println("******lfvEliminateNutAllergy RECIPES : " + lfvNutAllergyEliminationRecipes);
		
		//lfv eliminate Allergy Soy,Sesame,Egg-lfvOtherAllergyEliminationRecipes
		lfvOtherAllergyEliminationRecipes=filterRecipes(lfvToAddEliminationRecipes,TestUtil.LFV_OTHER_ALLERGY,true);
		System.out.println("******lfvEliminateNutAllergy RECIPES : " + lfvOtherAllergyEliminationRecipes);
		
		
	
		if (driver != null) {
			driver.quit();// closing driver at the end
		}
	
	}//method ends here
	
   
	public void recipeDataScraper(WebDriver driver) throws JsonParseException, JsonMappingException, IOException, SQLException {
		
		boolean lfvRecipesToAvoid = false ;
		
		//extracting recipe id from the current url
		String recipeUrl = driver.getCurrentUrl();
		//Split the URL by hyphen and 'r' to get the parts
		String[] parts = recipeUrl.split("-");
		// The recipe ID is the last part before 'r'
		String recipeId = parts[parts.length - 1].replace("r", "");
		System.out.println("*****************************************************************************************************************");
		//System.out.println("Recipe Id : " + recipeId);
		
		//Getting the recipe name
		WebElement recipeTitleElement = driver.findElement(By.xpath("//div[@id='recipehead']//span//span"));
		String recipeTitle = recipeTitleElement.getText();
		//System.out.println("Recipe Name : " + recipeTitle);
		//scraping "LFV recipes to avoid" by filtering the constants like (microwave,fried,...)from the recipe title, if it finds any match it returns true.
		lfvRecipesToAvoid = Arrays.stream(TestUtil.LFV_RECIPES_TO_AVOID).anyMatch(recipeTitle.toLowerCase()::contains);
		
		//getting preparation time
		String preperationTime = driver.findElement(By.xpath("//time[@itemprop='prepTime']")).getText();
		//System.out.println("Preparation Time : " + preperationTime);
		
		//getting cooking time
		String cookingTime = driver.findElement(By.xpath("//time[@itemprop='cookTime']")).getText();
		//System.out.println("Cooking Time : " + cookingTime);
		
		// Getting Ingredients
		List<WebElement> ingredintsLoc = driver.findElements(By.xpath("//span[@itemprop='recipeIngredient']"));

		String ingredients = "";
		for (WebElement e1 : ingredintsLoc) {
			ingredients = ingredients + "\n" + e1.getText();
		}
		//System.out.println("Ingredients : " + ingredients);

		List<WebElement> ingredintsNameLoc = driver.findElements(By.xpath("//span[@itemprop='recipeIngredient']/a/span"));

		String ingredientsName = "";
		for (WebElement e1 : ingredintsNameLoc) {
			ingredientsName = ingredientsName + "\n" + e1.getText();
		}
		//System.out.println("Ingredients Name : " + ingredientsName);
		
		//Getting Cuisine Category
		String cuisineCategory = driver.findElement(By.xpath("//div[@class='breadcrumb']/span[7]/a/span")).getText();
		//System.out.println(cuisineCategory);

		//Getting No of Servings
		String numOfServings = driver.findElement(By.xpath("//span[@id='ctl00_cntrightpanel_lblServes']")).getText();
		//System.out.println("No of Servings : "+numOfServings);
		
		//Getting tags
		List<WebElement> tagsLoc = driver.findElements(By.xpath("//div[@id='recipe_tags']/a"));
		String tags = "";
		for (WebElement tag : tagsLoc) {
			
		//scraping "LFV recipes to avoid" by filtering the constants like (microwave,fried,...)from each recipe tag, if it finds any match it returns true and stops checking the condition.
		if(!lfvRecipesToAvoid) {
			lfvRecipesToAvoid = Arrays.stream(TestUtil.LFV_RECIPES_TO_AVOID).anyMatch(tag.getText().toLowerCase()::contains);	
		}
		
		tags = tags + "\n" + tag.getText();
		}
		//System.out.println("Recipe Tags : " + tags);
		
		//Getting Recipe Description
		String recipeDescription = driver.findElement(By.id("recipe_description")).getText();
		//System.out.println("Recipe Description : " + recipeDescription );

		//Getting Preperation Method
		List<WebElement> prepMethod = driver.findElements(By.xpath("//*[@id='recipe_small_steps']/span[1]//span[@itemprop='text']"));
		String preparationMethod = "";
		for (WebElement method : prepMethod) {
			preparationMethod = preparationMethod + "\n" + method.getText();
		}
		//System.out.println("Preparation Method : " + preparationMethod );

		//Getting Nutrition values
		List<WebElement> nutritionLoc = driver.findElements(By.xpath("//*[@id='rcpnutrients']//tr"));
		String nutritionValues = "";
		for (WebElement nutrition : nutritionLoc) {
			nutritionValues = nutritionValues + "\n" + nutrition.getText();
		}
		//System.out.println("Nutrition Values : " + nutritionValues );

		//Recipe Url
		//System.out.println("Recipe Url: " + recipeUrl);
		
		// Determining the food category based on the tags and ingredients
				String foodCategory = "Vegetarian";//by default food category is vegetarian
				String combinedText = (tags + ingredientsName).toLowerCase();//combining tags and ingredientname for filtering
				//using streams to check if there is any match with the ingredients in arraylist and the string
				boolean isEggetarian = !Arrays.stream(TestUtil.EGGETARION_ELEMINATE_OPTIONS).anyMatch(combinedText::contains);
				boolean isVegan = !Arrays.stream(TestUtil.VEGAN_EMINATE_OPTIONS).anyMatch(combinedText::contains);
				if(combinedText.contains("egg") && isEggetarian ){
					foodCategory = "Eggetarian"; 
				} else if(combinedText.contains("jain")) { 
					foodCategory = "Jain"; 
				} else if(isVegan || combinedText.contains("vegan")
						||recipeUrl.contains("vegan")
						){ 
					foodCategory = "Vegan"; 
				} 
						
		//Store the details of scrapped recipes to the RecipesScrapped object
		RecipesScrapped recipe = new RecipesScrapped();
		recipe.setRecipeID(recipeId);
		recipe.setRecipeName(recipeTitle);
		recipe.setIngredientsName(ingredientsName);
		recipe.setIngredients(ingredients);
		recipe.setPreperationTime(preperationTime);
		recipe.setCookingTime(cookingTime);
		recipe.setNumOfServings(numOfServings);
		recipe.setRecipeDescription(recipeDescription);
		recipe.setPreparationMethod(preparationMethod);
		recipe.setNutritionValues(nutritionValues);
		recipe.setRecipeUrl(recipeUrl);
		recipe.setFoodCategory(foodCategory);
		recipe.setLfvRecipesToAvoid(lfvRecipesToAvoid);
		//System.out.println("----------End Scrapping data--------"+recipe.getFoodCategory());


		allRecipesList.add(recipe);
		System.out.println("Total no. of recipes: "+allRecipesList.size());
		
	//	db.insertRecipeData(recipeId, recipeTitle, preperationTime, cookingTime, ingredients,ingredientsName, cuisineCategory, numOfServings);
		
	}
	
	public List<RecipesScrapped> filterRecipes(List<RecipesScrapped> recipeList,String filterString, boolean toBeNotIncluded)
	{
		List<RecipesScrapped> filteredRecipes = null;
		//using java streams(lambda expression) for filtering data
		//using streams to check if there is any match with the ingredients in array list and the string
		System.out.println("toBeNotIncluded :"+toBeNotIncluded+" & FilterString Inside the filter recipes"+filterString);
	/*	if(toBeNotIncluded) {
		filteredRecipes	= recipeList.stream().filter(rec ->
		!Arrays.stream(filterString.toLowerCase().split(",")).anyMatch(rec.getIngredientsName().toLowerCase()::contains))
				.collect(Collectors.toList());
		}
		else {
		filteredRecipes = recipeList.stream().filter(rec ->
		Arrays.stream(filterString.toLowerCase().split(",")).anyMatch(rec.getIngredientsName().toLowerCase()::contains))
				.collect(Collectors.toList());
		}*/
		filteredRecipes = recipeList.stream()
			    .filter(rec -> {
			        boolean containsMatch = Arrays.stream(filterString.toLowerCase().split(","))
			                                      .anyMatch(rec.getIngredientsName().toLowerCase()::contains);
			        return toBeNotIncluded ? !containsMatch : containsMatch;
			    })
			    .collect(Collectors.toList());

		return filteredRecipes;
	
	}
	
}
