package net.thucydides.showcase.cucumber.pages;

import com.google.common.base.Function;

import cucumber.api.DataTable;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BitTabResourcePaths extends PageObject {

    private WebDriver driver;
    private WebDriverWait wait;
	private static Properties Param;
	public int waitFileLoad;
	public int timeOutSet;
	public int clipboardTime;

	public BitTabResourcePaths(WebDriver driver) throws IOException {
		Param = new Properties();
		FileInputStream fip = new FileInputStream(System.getProperty("user.dir")+"//Param.properties");
		Param.load(fip);
		this.waitFileLoad = Integer.parseInt(Param.getProperty("waitFileLoad"));
		this.timeOutSet = Integer.parseInt(Param.getProperty("timeOutSet"));
		this.clipboardTime = Integer.parseInt(Param.getProperty("clipboardTime"));
		
	    this.driver = driver;
	    this.wait =  new WebDriverWait(driver, timeOutSet);
	}
	
	public void userClickTabResourcePaths() throws Throwable {
		// Wait till loading image disappears
		By loadingImage = By.id("loading-div-background");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
		// Click tab "Client"
		driver.findElement(By.id("tab_header_resources")).click();
	}

	public void userFillFormDefinitionInSectionAFPTabResourcePaths(DataTable tabResourcePathsAfp) throws Throwable {
		// Wait till loading image disappears
		By loadingImage = By.id("loading-div-background");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
		List<List<String>> data = tabResourcePathsAfp.raw();
	  
	    int maxRowsOfData = data.size(); // getting how many rows
	    System.out.println("maxRowsOfData = " + maxRowsOfData);
		/*for (Map<String, String> data : tabResourcePathsAfp.asMaps(String.class, String.class)) {
			System.out.println("Name:" + data.get("Name"));
			System.out.println("Path:" + data.get("Path"));
			//driver.findElement(By.id("subDocType")).sendKeys(data.get("Name"));
			//driver.findElement(By.id("subDocType")).sendKeys(data.get("Path"));
		} // end for loop*/ 
	    //===========Remove all existing rows in order to add new rows as per feature file========================
	    int rowCount=driver.findElements(By.xpath("//table[@id='AFPFORMDEFPATH']/tbody/tr")).size();
	    System.out.println(rowCount); // no of rows include the row that contains button Add, Add Predefinded, Remove
	    // open panel
	    driver.findElement(By.xpath(".//*[@id='ui-accordion-afpResourcePath-header-0']")).click();
    
	    String checkBox = null;
	    for(int i=0; i<rowCount-1; i++){
	    	checkBox = "delCheckBox_AFPFORMDEFPATH_" + Integer.toString(i);
	    	System.out.println("checkbox: " + checkBox );
	    	driver.findElement(By.id(checkBox)).click();
	    }
	    
	    String removeButton = ".//*[@id='AFPFORMDEFPATH']/tbody/tr[" + rowCount +"]/td/button[3]";
	    driver.findElement(By.xpath(removeButton)).click();
	    //============Add the rows================================================================================
	    //driver.findElement(By.xpath(".//*[@id='AFPFORMDEFPATH']/tbody/tr/td/button[1]")).click();
	    String addButton = null;
	    String textName = null;
	    String textValue = null;
	    for(int j=0; j<maxRowsOfData-1; j++){
	    	textName = "textName_AFPFORMDEFPATH_"+ Integer.toString(j);
	    	textValue = "textValue_AFPFORMDEFPATH_"+ Integer.toString(j);
    		if(j==0)
    		{
    			driver.findElement(By.xpath(".//*[@id='AFPFORMDEFPATH']/tbody/tr/td/button[1]")).click();
    	    	driver.findElement(By.id(textName)).sendKeys("Client");
    	    	driver.findElement(By.id(textValue)).sendKeys("%BASEPATH%/vip/user/%CLIENT%/deflib");

    		}
    		else
    		{  // Continue here
    			System.out.println("================================================================================");
    			addButton = ".//*[@id='AFPFORMDEFPATH']/tbody/tr[" + Integer.toString(j+1) + "]/td/button[1]";
    			System.out.println("addButton = " + addButton);
    			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(addButton)));
    			driver.findElement(By.xpath(addButton)).click();
    			//wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
    			//wait.until(ExpectedConditions.presenceOfElementLocated(By.id("textName_AFPFORMDEFPATH_1")));
    			//driver.findElement(By.id("textName_AFPFORMDEFPATH_1")).sendKeys("Pae");
    			
    			//System.out.println("j = " + j);
    			
    			//System.out.println("textName = " + textName);
    			//System.out.println("textValue = " + textValue);
    			//driver.findElement(By.id("textName_AFPFORMDEFPATH_1")).sendKeys("Pae");
    	    	//driver.findElement(By.id(textName)).sendKeys("Client2");
    	    	//driver.findElement(By.id(textValue)).sendKeys("%BASEPATH%/vip/user/%CLIENT%/deflib");
    		}
    		
	    	//System.out.println(data.get(i).get(0)); // get column "Name"
    		//System.out.println(data.get(i).get(1)); // get column "Path"
    	
    	}
	    
	    
	}
	

}

