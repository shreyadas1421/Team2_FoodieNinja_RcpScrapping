package base;

import utils.LoggerLoad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {

	public static Properties prop;
	protected static WebDriver driver;

	public TestBase() {

		prop = new Properties();

		try {

			InputStream stream = TestBase.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(stream);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void initialization() {

		LoggerLoad.info("Inside init");
		LoggerLoad.info("The url is: " + prop.getProperty("urlnew"));
		//LoggerLoad.info("The url is: " + prop.getProperty("url"));

		String browserName = prop.getProperty("browser");

		System.out.println("Hi"+browserName);
		if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\Alice\\git\\team5_recipe_pickers\\src\\test\\resources\\Drivers\\chromedriver.exe");
			ChromeOptions options=new ChromeOptions();
			
			options.addArguments("--blink-settings=imagesEnabled=false");//disabling the images
			options.addArguments("--disable-images");
			options.addArguments("--disable-javascript");
			options.addArguments("--remote-allow-origins=*");
			options.addArguments("--headless");//executing in headless mode
			options.addArguments("--disable-popup-blocking");
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-extensions");

			driver=new ChromeDriver(options);
			//driver.get(prop.getProperty("url"));
			driver.get(prop.getProperty("urlnew"));
			System.out.println("Title is: " +driver.getTitle());

		} else if (browserName.equalsIgnoreCase("edge")) {

			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
			driver.get(prop.getProperty("url"));

		} else if (browserName.equalsIgnoreCase("safari")) {

			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
			driver.get(prop.getProperty("url"));


		}


	}

	public String getTitleOfCurrentPage() {

		return driver.getTitle();

	}

	public String getURLOfCurrentPage() {

		return driver.getCurrentUrl();

	}

	public static WebDriver getDriver() {
		return driver;
	}

}
