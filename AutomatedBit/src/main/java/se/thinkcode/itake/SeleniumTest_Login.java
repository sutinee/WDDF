package se.thinkcode.itake;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTest_Login {
		public static void main(String[] args){
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/Google/Driver/chromedriver.exe");
		WebDriver driver=new ChromeDriver();
			
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// Launch CSR 
		driver.manage().window().maximize();
		driver.get("http://wlo/tf/FANMedia?tx=Startup&cz=e0604132714130400120417080200&cmd=csrweb&doctype=list&csrfunction=logon-verify");
		
		driver.findElement(By.id("userName")).sendKeys("test"); 
		driver.findElement(By.id("password")).sendKeys("test789"); 
		driver.findElement(By.id("submit")).click();
		
		System.out.println("Login Successfully");
		//driver.quit();
	}

}