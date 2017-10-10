package net.thucydides.showcase.cucumber.steps.serenity;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import net.thucydides.showcase.cucumber.pages.BitHomePage;
import net.thucydides.showcase.cucumber.pages.BitTabClientPage;
import net.thucydides.showcase.cucumber.pages.BitTabDatabasePage;
import net.thucydides.showcase.cucumber.pages.BitTabDocTypesPage;
import net.thucydides.showcase.cucumber.pages.BitTabResourcePaths;
import net.thucydides.showcase.cucumber.pages.HomePage;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.DataTable;

public class BitSteps extends ScenarioSteps {
	
	//public static WebDriver driver;
	//public static WebDriverWait wait;
	private BitHomePage bitHomePage;
	private BitTabClientPage bitTabClientPage;
	private BitTabDocTypesPage bitTabDocTypesPage;
	private BitTabResourcePaths bitTabResourcePaths;
	private BitTabDatabasePage bitTabDatabasePage;
	
	

    @Step
    public void opens_home_page() throws Throwable {
    	//wait = new WebDriverWait(driver, 60);
    	bitHomePage.open();
    	bitHomePage.maximizeWebBrowser();
    	
    }
	
    @Step
    public void userStartsANewClientConfiguration() throws Throwable{
    	bitHomePage.userClicksStartANewClientConfigurationLink();
    }

    @Step
    public void userEnterClientDirectoryName(String newClientName) throws Throwable{
    	bitHomePage.userEnterClientDirectoryName(newClientName);
    	//bitTabClientPage.userClickTabClientBit();
    }
	
    @Step
    public void userFillGeneralInfoInTabClient(DataTable tabClientData) throws Throwable{
    	bitTabClientPage.userFillGeneralInfoInTabClient(tabClientData);
    }
    // ===============Tab Client ===========================================================
    @Step
    public void userClickTabClient() throws Throwable{
    	bitTabClientPage.userClickTabClient();
    }
    
    @Step
    public void userFillCSRWebConfigurationInfoInTabClient(DataTable tabClientDataCSRWeb) throws Throwable{
    	bitTabClientPage.userFillCSRWebConfigurationInfoInTabClient(tabClientDataCSRWeb);
    }
    
    @Step
    public void userFillDefaultClientNDMConfigurationInfoInTabClient(DataTable tabClientDataDefault) throws Throwable{
    	bitTabClientPage.userFillDefaultClientNDMConfigurationInfoInTabClient(tabClientDataDefault);
    }
    
    // ===============Tab Database ===========================================================
    @Step
    public void userClickTabDatabase() throws Throwable{
    	bitTabDatabasePage.userClickTabDatabaseBit();
    }
    
    @Step
    public void userFillConfigurationInfoInTabDatabase(DataTable tabDatabaseConfiguration) throws Throwable{
    	bitTabDatabasePage.userFillConfigurationInfoInTabDatabaseTest(tabDatabaseConfiguration);
    }
    
    @Step
    public void userFillAdditionalOptionsInTabDatabase(DataTable tabDatabaseAdditionalOption) throws Throwable{
    	bitTabDatabasePage.userFillAdditionalOptionsInTabDatabase(tabDatabaseAdditionalOption);
    }
    
    @Step
    public void userValidateDatabaseSetupInTabDatabase() throws Throwable{
    	bitTabDatabasePage.userValidateDatabaseSetupInTabDatabase();
    }
    
    // ===============Tab Resources Path ===========================================================
    @Step
    public void userClickTabResourcePaths() throws Throwable{
    	bitTabResourcePaths.userClickTabResourcePaths();
    }
    
    @Step
    public void userFillFormDefinitionInSectionAFPTabResourcePaths(DataTable tabResourcePathsAfp) throws Throwable{
    	bitTabResourcePaths.userFillFormDefinitionInSectionAFPTabResourcePaths(tabResourcePathsAfp);
    }
      
    
    // ===============Tab Doc Types ===========================================================    
    @Step
    public void userClickTabDocTypes() throws Throwable{
    	bitTabDocTypesPage.userClickTabDocTypes();
    }
    
    @Step
    public void userAddDocumentDefinitionInTabDocTypes(DataTable tabDoctypesDocumentConfiguration) throws Throwable{
    	bitTabDocTypesPage.userAddDocumentDefinitionInTabDocTypes(tabDoctypesDocumentConfiguration);
    }

   @Step
    public void userFillInTabFileOfNewDocumentType(DataTable tabFileNewDocType) throws Throwable{
    	bitTabDocTypesPage.userFillInTabFileOfNewDocumentType(tabFileNewDocType);
    } 

	
}