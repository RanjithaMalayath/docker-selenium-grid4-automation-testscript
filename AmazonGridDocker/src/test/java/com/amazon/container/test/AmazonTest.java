package com.amazon.container.test;



import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


import com.amazon.container.pages.AmazonPageObjects;

public class AmazonTest

{
	public WebDriver driver;
	String url = "https://www.amazon.in";
	@Parameters("browser")
	@BeforeTest
	public void launchbrowser(String br) throws MalformedURLException {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setBrowserName(br);
		URL huburl = new URL("http://localhost:4445");		
		driver = new RemoteWebDriver(huburl, cap);
		driver.get("https://www.amazon.in");
	
	}
    @Test
    public void amazonTest() throws InterruptedException {
    	AmazonPageObjects amazon = new AmazonPageObjects(driver);

    	
       // amazon.launchApp();
    	amazon.launchApp();
        amazon.clickHamburgerMenu();
        amazon.selectCategory();
        amazon.clickTelevision();
        amazon.applyFilter();
        amazon.clickSecHighestItem();
        Assert.assertTrue(amazon.isAboutThisItemPresent());
    }
        
    @AfterTest
    public void closeApp() throws InterruptedException {
        driver.quit();
    }  

}

