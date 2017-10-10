package net.thucydides.showcase.cucumber.pages;

import com.google.common.base.Function;

import cucumber.api.DataTable;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

//@DefaultUrl("http://wlo:7001/bit/")
@DefaultUrl("http://wlo:7001/bit/loadClient?clientName=gen-taitest")
public class BitHomePage extends PageObject {

    private WebDriver driver;
    private WebDriverWait wait;
	private static Properties Param;
	public int waitFileLoad;
	public int timeOutSet;
	public int clipboardTime;

	public BitHomePage(WebDriver driver) throws IOException {
		Param = new Properties();
		FileInputStream fip = new FileInputStream(System.getProperty("user.dir")+"//Param.properties");
		Param.load(fip);
		this.waitFileLoad = Integer.parseInt(Param.getProperty("waitFileLoad"));
		this.timeOutSet = Integer.parseInt(Param.getProperty("timeOutSet"));
		this.clipboardTime = Integer.parseInt(Param.getProperty("clipboardTime"));
		
	    this.driver = driver;
	    this.wait =  new WebDriverWait(driver, timeOutSet);

	}

	public void maximizeWebBrowser() throws Throwable{		
		// Click accept T&C
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(timeOutSet, TimeUnit.SECONDS);
	}
	
	public void userClicksStartANewClientConfigurationLink() throws Throwable{
		driver.findElement(By.xpath("html/body/div[11]/div[3]/div/button[1]")).click();
		driver.findElement(By.linkText("Start a new Client Configuration")).click();	
	}
	
	public void userEnterClientDirectoryName(String existingClient) throws Throwable{
		driver.findElement(By.id("clientDirNameCheck")).sendKeys(existingClient);
		driver.findElement(By.xpath("html/body/div[11]/div[3]/div/button[1]")).click();
	}

}

