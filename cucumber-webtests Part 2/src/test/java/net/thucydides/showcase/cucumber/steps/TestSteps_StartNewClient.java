package net.thucydides.showcase.cucumber.steps;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.Assert;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.showcase.cucumber.pages.BitTabClientPage;
import net.thucydides.showcase.cucumber.steps.serenity.BitSteps;

public class TestSteps_StartNewClient {
	//private BitTabClient newClient;
    @Steps
    BitSteps userBit;
    
	@Given("^User is logged on$")
	public void userIsLoggedOn() throws Throwable{
		userBit.opens_home_page();
		//enter_username_Password();
		
	}
	
	@Given("^User starts a new Client Configuration$")
	public void userStartsANewClientConfiguration() throws Throwable {
		userBit.userStartsANewClientConfiguration();
	
	}
	
	@When("^User enters Client Directory Name \"(.*?)\"$")
	public void userEnterClientDirectoryName(String newClientName) throws Throwable {
		userBit.userEnterClientDirectoryName(newClientName);

	}
	
	@When("^User fills general info in tab Client$")
	public void userFillGeneralInfoInTabClient(DataTable tabClientData) throws Throwable {
		userBit.userFillGeneralInfoInTabClient(tabClientData);

	}
	
	@When("^User clicks tab Client$")
	public void userClickTabClient() throws Throwable {
		userBit.userClickTabClient();

	}
	
	@When("^User fills CSR Web Configuration info in tab Client$")
	public void userFillCSRWebConfigurationInfoInTabClient(DataTable tabClientDataCSRWeb) throws Throwable {
		userBit.userFillCSRWebConfigurationInfoInTabClient(tabClientDataCSRWeb);

	}
	
	@When("^User fills Default Client NDM Configuration info in tab Client$")
	public void userFillDefaultClientNDMConfigurationInfoInTabClient(DataTable tabClientDataDefault) throws Throwable {
		userBit.userFillDefaultClientNDMConfigurationInfoInTabClient(tabClientDataDefault);
	}
	//======================Tab Database===============================================================================
	@When("^User clicks tab Database$")
	public void userClickTabDatabase() throws Throwable {
		userBit.userClickTabDatabase();
	}
	
	@When("^User fills Configuration info in tab Database$")
	public void userFillConfigurationInfoInTabDatabase(DataTable tabDatabaseConfiguration) throws Throwable {
		userBit.userFillConfigurationInfoInTabDatabase(tabDatabaseConfiguration);
	}
	
	@When("^User fills Additional Options in tab Database$")
	public void userFillAdditionalOptionsInTabDatabase(DataTable tabDatabaseAdditionalOption) throws Throwable {
		userBit.userFillAdditionalOptionsInTabDatabase(tabDatabaseAdditionalOption);
	}
	
	@When("^User validates Database setup in tab Database$")
	public void userValidateDatabaseSetupInTabDatabase() throws Throwable {
		userBit.userValidateDatabaseSetupInTabDatabase();
	}
	//======================Resource Paths========================================================================
	@When("^User clicks tab Resource Paths$")
	public void userClickTabResourcePaths() throws Throwable {
		userBit.userClickTabResourcePaths();
	}
	
	@When("^User fills Form Definition in section AFP tab Resource Paths$")
	public void userFillFormDefinitionInSectionAFPTabResourcePaths(DataTable tabResourcePathsAfp) throws Throwable {
		userBit.userFillFormDefinitionInSectionAFPTabResourcePaths(tabResourcePathsAfp);
	}
	
	//======================Tab Doc Types===========================================================
	@When("^User clicks tab Doc Types$")
	public void userClickTabDocTypes() throws Throwable {
		userBit.userClickTabDocTypes();
	}
	
	@When("^User add new Document Definition$")
	public void userAddDocumentDefinitionInTabDocTypes(DataTable tabDoctypesDocumentConfiguration) throws Throwable {
		userBit.userAddDocumentDefinitionInTabDocTypes(tabDoctypesDocumentConfiguration);
	}

	@When("^User fill in tab file of new document type$")
	public void userFillInTabFileOfNewDocumentType(DataTable tabFileNewDocType) throws Throwable {
		userBit.userFillInTabFileOfNewDocumentType(tabFileNewDocType);
	}



}
	

