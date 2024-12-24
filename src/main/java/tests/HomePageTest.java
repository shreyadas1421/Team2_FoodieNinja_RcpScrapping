package tests;

import org.pageobjects.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import base.TestBase;
import pages.*;

public class HomePageTest extends TestBase {

	HomePage homePage;

	public HomePageTest() {
		super();
	}

	@BeforeClass
	public void setup() {
		TestBase.initialization();
		homePage = new HomePage();
	}
	
	@AfterClass
	public void teardown() {
		
	}

	@Test(priority=1)
	public void RecipeAtoETest() throws Exception {
		
		newurl recipe = new newurl(TestBase.getDriver());
		recipe.click_AtoZ_recipes();
		recipe.jgetRecipieInfo();
	}

	
}
