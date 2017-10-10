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

public class BitTabClientPage extends PageObject {

    private WebDriver driver;
    private WebDriverWait wait;
	private static Properties Param;
	public int waitFileLoad;
	public int timeOutSet;
	public int clipboardTime;

	public BitTabClientPage(WebDriver driver) throws IOException {
		Param = new Properties();
		FileInputStream fip = new FileInputStream(System.getProperty("user.dir")+"//Param.properties");
		Param.load(fip);
		this.waitFileLoad = Integer.parseInt(Param.getProperty("waitFileLoad"));
		this.timeOutSet = Integer.parseInt(Param.getProperty("timeOutSet"));
		this.clipboardTime = Integer.parseInt(Param.getProperty("clipboardTime"));
		
	    this.driver = driver;
	    this.wait =  new WebDriverWait(driver, timeOutSet);

	}

	//=========================Tab Client ===================================================================================
	
	public void userClickTabClient() throws Throwable {
		// Wait till loading image disappears
		By loadingImage = By.id("loading-div-background");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
		// Click tab "Client"
		driver.findElement(By.id("tab_header_client")).click();
	}
	
	public void userFillGeneralInfoInTabClient(DataTable tabClientData) throws Throwable {
		
		for (Map<String, String> data : tabClientData.asMaps(String.class, String.class)) {
			driver.findElement(By.id("clientResourceDirName")).sendKeys(data.get("National Resources Client Directory Name")); 
		    driver.findElement(By.id("mrdClientName")).sendKeys(data.get("Client Full Name"));
			}

	}
	
	public void userFillCSRWebConfigurationInfoInTabClient(DataTable tabClientDataCSRWeb) throws Throwable {
		
		for (Map<String, String> data : tabClientDataCSRWeb.asMaps(String.class, String.class)) {
		    driver.findElement(By.id("clientCSRUserName")).sendKeys(data.get("CSR Username"));
		    driver.findElement(By.id("clientLogo")).sendKeys(data.get("Client Logo Filename"));
		    
		    Select oSelect = new Select(driver.findElement(By.id("custServTextType")));
		    oSelect.selectByVisibleText(data.get("Customer Service Text Type"));	    
		    driver.findElement(By.id("mainSearchDateRange")).sendKeys(data.get("Main Search Date Range (months)"));
			if(data.get("Batch Retention").equals("ticked"))
			{ // must be ticked
				if (!driver.findElement(By.id("csrwpBatchRetention")).isSelected())
				{
				     driver.findElement(By.id("csrwpBatchRetention")).click();
				}
			}
			else
			{ // must not be unticked
				if (driver.findElement(By.id("csrwpBatchRetention")).isSelected())
				{
				     driver.findElement(By.id("csrwpBatchRetention")).click();
				}
			}
			
			if(data.get("Role-based Access Control (RBAC)").equals("ticked"))
			{ // must be ticked
				if (!driver.findElement(By.id("rbacSupport")).isSelected())
				{
				     driver.findElement(By.id("rbacSupport")).click();
				}
			}
			else
			{ // must not be ticked
				if (driver.findElement(By.id("rbacSupport")).isSelected())
				{
				     driver.findElement(By.id("rbacSupport")).click();
				}
			}
			
			if(data.get("Master Report").equals("ticked"))
			{ // must be ticked
				if (!driver.findElement(By.id("masterReport")).isSelected())
				{
				     driver.findElement(By.id("masterReport")).click();
				}
			}
			else
			{ // must not be ticked
				if (driver.findElement(By.id("masterReport")).isSelected())
				{
				     driver.findElement(By.id("masterReport")).click();
				}
			}
			
			if(data.get("Admin Reports").equals("ticked"))
			{ // must be ticked
				if (!driver.findElement(By.id("adminReport")).isSelected())
				{
				     driver.findElement(By.id("adminReport")).click();
				}
			}
			else
			{ // must not be ticked
				if (driver.findElement(By.id("adminReport")).isSelected())
				{
				     driver.findElement(By.id("adminReport")).click();
				}
			}
			
			if(data.get("Test Script").equals("ticked"))
			{ // must be ticked
				if (!driver.findElement(By.id("testScript")).isSelected())
				{
				     driver.findElement(By.id("testScript")).click();
				}
			}
			else
			{ // must not be ticked
				if (driver.findElement(By.id("testScript")).isSelected())
				{
				     driver.findElement(By.id("testScript")).click();
				}
			}
			
			if(data.get("Corp Numbers").equals("ticked"))
			{ // must be ticked
				if (!driver.findElement(By.id("enableCorpNumberLink")).isSelected())
				{
				     driver.findElement(By.id("enableCorpNumberLink")).click();
				}
			}
			else
			{ // must not be ticked
				if (driver.findElement(By.id("enableCorpNumberLink")).isSelected())
				{
				     driver.findElement(By.id("enableCorpNumberLink")).click();
				}
			} 

			} // end for loop
	}
	
	public void userFillDefaultClientNDMConfigurationInfoInTabClient(DataTable tabClientDataDefault) throws Throwable {
		for (Map<String, String> data : tabClientDataDefault.asMaps(String.class, String.class)) {
			Select aSelect = new Select(driver.findElement(By.id("ndmType")));
		    aSelect.selectByVisibleText(data.get("NDM Type"));
		    
		    driver.findElement(By.id("otskcFileName")).sendKeys(data.get("FILENAME"));
			Select bSelect = new Select(driver.findElement(By.id("otskcFileType")));
		    bSelect.selectByVisibleText(data.get("File Type"));
			}
	}
	
	//======================Tab Database===============================================================================
	public void userClickTabDatabaseBit() throws Throwable {
		// Wait till loading image disappears
		By loadingImage = By.id("loading-div-background");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
		// Click tab "Client"
		driver.findElement(By.id("tab_header_dbsettings")).click();
	}
	
	public void userFillConfigurationInfoInTabDatabaseTest(DataTable tabDatabaseConfiguration) throws Throwable {
		for (Map<String, String> data : tabDatabaseConfiguration.asMaps(String.class, String.class)) {
			Select aSelect = new Select(driver.findElement(By.id("devDbName")));
		    aSelect.selectByVisibleText(data.get("Database Name"));
			}
	}
	
	public void userFillAdditionalOptionsInTabDatabase(DataTable tabDatabaseAdditionalOption) throws Throwable {
		for (Map<String, String> data : tabDatabaseAdditionalOption.asMaps(String.class, String.class)) {	    		
			if(data.get("Keep Existing Security Setting").equals("ticked"))
			{ // must be ticked
				if (!driver.findElement(By.id("keepExistDbSetting")).isSelected())
				{
				     driver.findElement(By.id("keepExistDbSetting")).click();
				}
			}
			else
			{ // must not be unticked
				if (driver.findElement(By.id("keepExistDbSetting")).isSelected())
				{
				     driver.findElement(By.id("keepExistDbSetting")).click();
				}
			}
		} // end for loop
	}
	
	public void userValidateDatabaseSetupInTabDatabase() throws Throwable {
		driver.findElement(By.id("checkDatabaseBtn")).click();
		By loadingImage = By.id("loading-div-background");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
		//Verify validation result
		//String validateResult = driver.findElement(By.xpath(".//*[@id='validateReportResult']/div")).getText();
		//if(validateResult.contains("This database is currently setup for the following clients"))
		//{ 
		driver.findElement(By.id("cleanDatabaseBtn")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='dialogMessage']")));
		driver.findElement(By.xpath("html/body/div[11]/div[3]/div/button[1]")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
		//}
	}
	

}

