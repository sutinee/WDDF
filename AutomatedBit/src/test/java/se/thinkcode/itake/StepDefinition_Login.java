package se.thinkcode.itake;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;	

public class StepDefinition_Login {
	public static WebDriver driver;
	@Given("^User is on Home Page$")
	public void user_is_on_Home_Page() throws Throwable {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/Google/Driver/chromedriver.exe");
		driver=new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.manage().window().maximize();
		driver.get("http://wlo/tf/FANMedia?tx=Startup&cz=e0604132714130400120417080200&cmd=csrweb&doctype=list&csrfunction=logon-verify");
	}
	
	@When("^User enters UserName and Password$")
	public void user_Navigate_to_LogIn_Page() throws Throwable {
		driver.findElement(By.id("userName")).sendKeys("test"); 
		driver.findElement(By.id("password")).sendKeys("test789"); 
		driver.findElement(By.id("submit")).click();
	}
	
	@Then("^Message displayed Login Successfully$")
	public void message_displayed_Login_Successfully() throws Throwable {
		System.out.println("Login Successfully");
	}


}
