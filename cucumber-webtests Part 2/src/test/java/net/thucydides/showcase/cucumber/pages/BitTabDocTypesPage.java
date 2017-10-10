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

public class BitTabDocTypesPage extends PageObject {

    private WebDriver driver;
    private WebDriverWait wait;
	private static Properties Param;
	public int waitFileLoad;
	public int timeOutSet;
	public int clipboardTime;

	public BitTabDocTypesPage(WebDriver driver) throws IOException {
		Param = new Properties();
		FileInputStream fip = new FileInputStream(System.getProperty("user.dir")+"//Param.properties");
		Param.load(fip);
		this.waitFileLoad = Integer.parseInt(Param.getProperty("waitFileLoad"));
		this.timeOutSet = Integer.parseInt(Param.getProperty("timeOutSet"));
		this.clipboardTime = Integer.parseInt(Param.getProperty("clipboardTime"));
		
	    this.driver = driver;
	    this.wait =  new WebDriverWait(driver, timeOutSet);

	}

	//======================Tab Doc Types===============================================================================
	public void userClickTabDocTypes() throws Throwable {
			By loadingImage = By.id("loading-div-background");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
			driver.findElement(By.id("tab_header_doctypes")).click();		
	}
	
	public void userAddDocumentDefinitionInTabDocTypes(DataTable tabDoctypesDocumentConfiguration) throws Throwable {
		By loadingImage = By.id("loading-div-background");
		
		for (Map<String, String> data : tabDoctypesDocumentConfiguration.asMaps(String.class, String.class)) {
		    Select aSelect = new Select(driver.findElement(By.id("fileType_doctype")));
		    aSelect.selectByVisibleText(data.get("File Format"));
		    
		    Select bSelect = new Select(driver.findElement(By.id("docType")));
		    bSelect.selectByVisibleText(data.get("DocumentType-Pattern"));

		    driver.findElement(By.id("subDocType")).sendKeys(data.get("Subdocument Type"));
		    // Click button Add Doc Config
		    driver.findElement(By.id("button-addDoctypeConfig")).click();
		    wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
		} // end for loop
		// Document Configuration
		
		// ----------------- Open tab of new doc config ---------------------------------------------------------------
		/*if(docType.contains("statement"))
		{
			String docTypeCss = "#statement_-_-"+subDocType+" > #docTypeConfigureHeader > table > tbody > tr > td";
			driver.findElement(By.cssSelector(docTypeCss)).click();
		}
		
		if(docType.contains("tax"))
		{
			String docTypeCss = "#tax_-_-"+subDocType+" > #docTypeConfigureHeader > table > tbody > tr > td";
			driver.findElement(By.cssSelector(docTypeCss)).click();
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));*/
		
	//----------------------------------------------------------------------------------------------------------------	
	}
	
	public void userFillInTabFileOfNewDocumentType(DataTable tabFileNewDocType) throws Throwable {
		// New doc type panel is opened by default. No need to click panel of the new doc
		By loadingImage = By.id("loading-div-background");
		
		for (Map<String, String> data : tabFileNewDocType.asMaps(String.class, String.class)) {
			driver.findElement(By.id("descText")).sendKeys(data.get("Description"));
		    
			Select aSelect = new Select(driver.findElement(By.id("recdelimAfp")));
		    aSelect.selectByVisibleText(data.get("recdelim"));
		    
		    //Select bSelect = new Select(driver.findElement(By.id("formdef")));
		    //System.out.println(data.get("formdef"));
		    //bSelect.selectByVisibleText(data.get("formdef"));

			driver.findElement(By.id("dataFilenameText")).sendKeys(data.get("Data Filename"));
			driver.findElement(By.id("subProductFieldNumber1")).sendKeys(data.get("Field Number (Sub1)"));		
			driver.findElement(By.id("subProductKeyMatch1")).sendKeys(data.get("Key Match (Sub1)"));
			driver.findElement(By.id("subProductDescription1")).sendKeys(data.get("Description (Sub1)"));
			
			if(data.get("Reconciliation").equals("ticked"))
			{ // must be ticked
				if (!driver.findElement(By.id("reconcile")).isSelected())
				{
				     driver.findElement(By.id("reconcile")).click();
				}
			}
			else
			{ // must not be unticked
				if (driver.findElement(By.id("reconcile")).isSelected())
				{
				     driver.findElement(By.id("reconcile")).click();
				}
			}
			
		    Select cSelect = new Select(driver.findElement(By.id("reconcileProcessor")));
		    cSelect.selectByVisibleText(data.get("Reconcile Processor"));
			
		    Select dSelect = new Select(driver.findElement(By.id("reconcileAbort")));
		    dSelect.selectByVisibleText(data.get("Reconcile Processor Abort"));
		    
		    Select eSelect = new Select(driver.findElement(By.id("reconcileFailAction")));
		    eSelect.selectByVisibleText(data.get("Reconcile Fail Action"));
		} // end for loop
		
	}
	
}

