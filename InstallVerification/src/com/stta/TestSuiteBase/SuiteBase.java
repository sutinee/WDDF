//Find More Tutorials On WebDriver at -> http://software-testing-tutorials-automation.blogspot.com
package com.stta.TestSuiteBase;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.stta.utility.Read_XLS;

public class SuiteBase {	
	public static Read_XLS TestSuiteListExcel=null;
	public static Read_XLS TestCaseListExcelCSR=null;
	public static Read_XLS TestCaseListExcelECC=null;
	public static Read_XLS TestCaseListExcelFanWeb=null;
	public static Read_XLS TestCaseListExcelVision=null;
	public static Logger Add_Log = null;
	public boolean BrowseralreadyLoaded=false;
	public static Properties Param = null;
	public static Properties Object = null;
	public static WebDriver driver=null;
	public static WebDriver ExistingchromeBrowser;
	public static WebDriver ExistingFFBrowser;
	public static WebDriver ExistingIEBrowser;
	public int waitFileLoad;
	public int timeOutSet;
	public int clipboardTime;
	//public String userDT;
	//public String userPWD;
	public static DateFormat dateFolder = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
	public static Date date = new Date();
	public static String dateFolderName = dateFolder.format(date);
	public static String dateFolderNameFinal = new StringBuilder().append(dateFolderName).append("_PRODReport").toString();
	//public static WebDriverWait wait = null;
	
	public void init() throws IOException{
		//To Initialize logger service.
		Add_Log = Logger.getLogger("rootLogger");				
				
		//Please change file's path strings bellow If you have stored them at location other than bellow.
		//Initializing Test Suite List(TestSuiteList.xls) File Path Using Constructor Of Read_XLS Utility Class.
		TestSuiteListExcel = new Read_XLS(System.getProperty("user.dir")+"\\src\\com\\stta\\ExcelFiles\\TestSuiteList.xls");
		//Initializing Test Suite One(SuiteCSR.xls) File Path Using Constructor Of Read_XLS Utility Class.
		TestCaseListExcelCSR = new Read_XLS(System.getProperty("user.dir")+"\\src\\com\\stta\\ExcelFiles\\SuiteCSR.xls");
		//Initializing Test Suite One(SuiteECC.xls) File Path Using Constructor Of Read_XLS Utility Class.
		TestCaseListExcelECC = new Read_XLS(System.getProperty("user.dir")+"\\src\\com\\stta\\ExcelFiles\\SuiteECC.xls");
		//Initializing Test Suite One(SuiteFanWeb.xls) File Path Using Constructor Of Read_XLS Utility Class.
		TestCaseListExcelFanWeb = new Read_XLS(System.getProperty("user.dir")+"\\src\\com\\stta\\ExcelFiles\\SuiteFanWeb.xls");
		//Initializing Test Suite One(SuiteFanVision.xls) File Path Using Constructor Of Read_XLS Utility Class.
		TestCaseListExcelVision = new Read_XLS(System.getProperty("user.dir")+"\\src\\com\\stta\\ExcelFiles\\SuiteVision.xls");
		//Bellow given syntax will Insert log In applog.log file.
		Add_Log.info("All Excel Files Initialised successfully.");
		
		//Initialize Param.properties file. PAE: NOT IN USE
		Param = new Properties();
		FileInputStream fip = new FileInputStream(System.getProperty("user.dir")+"//src//com//stta//property//Param.properties");
		Param.load(fip);
		Add_Log.info("Param.properties file loaded successfully.");
		
		this.waitFileLoad = Integer.parseInt(Param.getProperty("waitFileLoad"));
		this.timeOutSet = Integer.parseInt(Param.getProperty("timeOutSet"));
		this.clipboardTime = Integer.parseInt(Param.getProperty("clipboardTime"));
		
		// Proxy login (FanWeb on VCloud)
		//this.userDT = Param.getProperty("userDT");
		//this.userPWD = Param.getProperty("userPWD");


		//Initialize Objects.properties file.
		Object = new Properties();
		fip = new FileInputStream(System.getProperty("user.dir")+"//src//com//stta//property//Objects.properties");
		Object.load(fip);
		Add_Log.info("Objects.properties file loaded successfully.");
		
		// 29 Nov reports change path
		try {

			String content = "report.date=" + dateFolderNameFinal;

			File file = new File(System.getProperty("user.dir")+"//src//com//stta//property//report.properties");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("create report.properties Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadWebBrowser(String Browser){
		//Check If any previous webdriver browser Instance Is exist then run new test In that existing webdriver browser Instance.
			if(Browser.equalsIgnoreCase("FF") && ExistingFFBrowser!=null){
				driver = ExistingFFBrowser;
				return;
			}else if(Browser.equalsIgnoreCase("chrome") && ExistingchromeBrowser!=null){
				driver = ExistingchromeBrowser;
				return;
			}else if(Browser.equalsIgnoreCase("IE") && ExistingIEBrowser!=null){
				driver = ExistingIEBrowser;
				return;
			}		
		
		
			if(Browser.equalsIgnoreCase("FF")){
				//To Load Firefox driver Instance. 
				driver = new FirefoxDriver();
				ExistingFFBrowser=driver;
				Add_Log.info("Firefox Driver Instance loaded successfully.");
				
			}else if(Browser.equalsIgnoreCase("Chrome")){
				//To Load Chrome driver Instance.
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//BrowserDrivers//chromedriver.exe");
				driver = new ChromeDriver();
				ExistingchromeBrowser=driver;
				Add_Log.info("Chrome Driver Instance loaded successfully.");
				
			}else if(Browser.equalsIgnoreCase("IE")){
				//To Load IE driver Instance.
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"//BrowserDrivers//IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				ExistingIEBrowser=driver;
				Add_Log.info("IE Driver Instance loaded successfully.");
				
			}			
			driver.manage().timeouts().implicitlyWait(timeOutSet, TimeUnit.SECONDS);
			driver.manage().window().maximize();
		
			
			
			
	}
	

	 public String takeScreenshot (String testCaseName, String stepName) throws InterruptedException, IOException 
	 { 
	  File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	  DateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
	  // 11/28
	  //DateFormat dateFolder = new SimpleDateFormat("dd-MMM-yyyy");
	  //Date date = new Date();
	  //String dateFolderName = dateFolder.format(date);
	  //String dateFolderNameFinal = new StringBuilder().append(dateFolderName).append("PRODReport").toString();
	  //String fileName = System.getProperty("user.dir") + "\\screenshots\\"+ "\\" +dateFolderNameFinal+ "\\" + testCaseName +"_" + stepName + "_" + dateFormat.format(new Date()) +".jpg";
	  String fileName = "C:\\InstallVerificationReports"+ "\\" + dateFolderNameFinal + "\\screenshots\\" + testCaseName +"_" + stepName + "_" + dateFormat2.format(new Date()) +".jpg";
	  //String fileName = System.getProperty("user.dir") + "\\screenshots\\"+ testCaseName +"_" + stepName + "_" + dateFormat.format(new Date()) +".jpg";
	  FileUtils.copyFile(screenshot, new File(fileName));
	  Add_Log.info("\nScreenshot is captured and stored in your" + fileName);
	  return fileName;
	 }
	 
	 public void reportOutput (String screenshotName, String stepPassDesc, String stepFailDesc) throws InterruptedException, IOException 
	 { 
		 	if(screenshotName.equals(""))
		 	{
		 		Reporter.log("<br><font color = red>FAIL: NO SCREENSHOT TAKEN</font><br>");
	 		}
		 	else
		 	{
		 		if(!screenshotName.contains("Fail"))
		 		{
		 			Reporter.log("<br><a href=\""+ screenshotName +"\" target=\"_blank\">" + stepPassDesc + "</a><br>");
		 		}
		 		else
		 		{
		 			Reporter.log("<br><a href=\""+ screenshotName +"\" target=\"_blank\"><font color = red>" + stepFailDesc + "</font></a><br>");
		 			//Reporter.log("<br><font color = red>"+ stepFailDesc + "<font color = red><br>");
		 		}
		 	}
		 
	 }
	 
	 public String verifyPDFContent() throws InterruptedException
	 {
		 
		 WebDriverWait wait = new WebDriverWait(driver, timeOutSet);
		 wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body")));
		 driver.findElement(By.xpath("//body")).click();
		 Thread.sleep(clipboardTime);
		 driver.findElement(By.xpath("//body")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
		 Thread.sleep(clipboardTime);
		 driver.findElement(By.xpath("//body")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
		 Thread.sleep(clipboardTime);
		 driver.findElement(By.xpath("//body")).click();
		 Thread.sleep(clipboardTime);
		 String PDFContent = getClipBoard();
		 Add_Log.info("PDF can be opened. As expected");
		 return PDFContent;
	 }
	 
	 
	public void closeWebBrowser(){
		driver.quit();
		//null browser Instance when close.
		ExistingchromeBrowser=null;
		ExistingFFBrowser=null;
		ExistingIEBrowser=null;
	}
	
	public boolean existsElement(String linkTxt) {
	    try {
	        //driver.findElement(By.id(id));
	    	driver.findElement(By.linkText(linkTxt));
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	    return true;
	}

	//NOT IN USE getElementByXPath function for static xpath
	
	public WebElement getElementByXPath(String Key){
		try{
			//This block will find element using Key value from web page and return It.
			return driver.findElement(By.xpath(Object.getProperty(Key)));
		}catch(Throwable t){
			//If element not found on page then It will return null.
			Add_Log.debug("Object not found for key --"+Key);
			return null;
		}
	}
	
	//getElementByXPath function for dynamic xpath
	public WebElement getElementByXPath(String Key1, int val, String key2){
		try{
			//This block will find element using values of Key1, val and key2 from web page and return It.
			return driver.findElement(By.xpath(Object.getProperty(Key1)+val+Object.getProperty(key2)));
		}catch(Throwable t){
			//If element not found on page then It will return null.
			Add_Log.debug("Object not found for custom xpath");
			return null;
		}
	}
	
	//Call this function to locate element by ID locator.
	public WebElement getElementByID(String Key){
		try{
			return driver.findElement(By.id(Object.getProperty(Key)));
		}catch(Throwable t){
			Add_Log.debug("Object not found for key --"+Key);
			return null;
		}
	}
	
	//Call this function to locate element by Name Locator.
	public WebElement getElementByName(String Key){
		try{
			return driver.findElement(By.name(Object.getProperty(Key)));
		}catch(Throwable t){
			Add_Log.debug("Object not found for key --"+Key);
			return null;
		}
	}
	
	//Call this function to locate element by cssSelector Locator.
	public WebElement getElementByCSS(String Key){
		try{
			return driver.findElement(By.cssSelector(Object.getProperty(Key)));
		}catch(Throwable t){
			Add_Log.debug("Object not found for key --"+Key);
			return null;
		}
	}
	
	//Call this function to locate element by ClassName Locator.
	public WebElement getElementByClass(String Key){
		try{
			return driver.findElement(By.className(Object.getProperty(Key)));
		}catch(Throwable t){
			Add_Log.debug("Object not found for key --"+Key);
			return null;
		}
	}
	
	//Call this function to locate element by tagName Locator.
	public WebElement getElementByTagName(String Key){
		try{
			return driver.findElement(By.tagName(Object.getProperty(Key)));
		}catch(Throwable t){
			Add_Log.debug("Object not found for key --"+Key);
			return null;
		}
	}
	
	//Call this function to locate element by link text Locator.
	public WebElement getElementBylinkText(String Key){
		try{
			return driver.findElement(By.linkText(Object.getProperty(Key)));
		}catch(Throwable t){
			Add_Log.debug("Object not found for key --"+Key);
			return null;
		}
	}
	
	//Call this function to locate element by partial link text Locator.
	public WebElement getElementBypLinkText(String Key){
		try{
			return driver.findElement(By.partialLinkText(Object.getProperty(Key)));
		}catch(Throwable t){
			Add_Log.debug("Object not found for key --"+Key);
			return null;
		}
	}
	
	public static String getClipBoard(){
	    try {
	        return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
	    } catch (HeadlessException e) {
	        e.printStackTrace();            
	    } catch (UnsupportedFlavorException e) {
	        e.printStackTrace();            
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

}
