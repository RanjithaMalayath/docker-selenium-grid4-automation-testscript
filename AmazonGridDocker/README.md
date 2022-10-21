# Selenium Grid4 Docker Project - nonVNC Docker Approach


## Prerequisites

- Java JDK
- Eclipse 
- Maven 
- TestNG
- Docker 

## Creating Maven Project for Amazon TestCase

- Open Eclipse 
   - File -> New -> Project -> Maven Project -> Create Maven project with groupId as "com.amazon" and artifactId as "AmazonGridDocker"->Create project.
- Add all required libraries to POM.xml file generated by default.
   - Add selenium, testng dependencies.
- Create two packages under src/test/java
   - com.amazon.container.pages -> to store Page Objects and methods classes.
   - com.amazon.container.test -> to store all Testcases classes. Add desired capabilities to setup Selenium Grid in this class.
- Create Page Object class for the testcase under com.amazon.container.pages as "AmazonPageObjects".
- Create Test case class under com.amazon.container.test as "AmazonTest".
- Create a new xml file for testNG Test Suite, "amazon.xml".

## Docker Set Up

- Login to docker portal : https://hub.docker.com/ and sign in to the account.



## Create docker-compose.yml file

- Step I : Create hub container "seleniumHub" and mapped port no 4445 to default port 4444.
- Step II : Create node containers for chrome and firefox to run the testcases in nonVNC view and provide port no 7900.
 

#### Code Snippets

###### docker-compose.yml

```yml
  version: "4"
services:
      HubService:
            image: selenium/hub:4.0.0-rc-2-20210930
            container_name: seleniumHub
            ports: 
                  - "4445:4444"
                  - "4442:4442"
                  - "4443:4443"

      ChromeService:
            image: selenium/node-chrome:4.0.0-rc-2-20210930
            shm_size: "2gb"
            ports:
                  - "5900"
                  - "7900"
            environment:
                  - SE_EVENT_BUS_HOST=seleniumHub
                  - SE_EVENT_BUS_PUBLISH_PORT=4442
                  - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
                  - SE_NODE_MAX_SESSIONS=2
            depends_on:
                  - HubService
      FirefoxService:
            image: selenium/node-firefox:4.0.0-rc-2-20210930
            shm_size: "2gb"
            ports:
                  - "5900"
                  - "7900"
            environment:
                  - SE_EVENT_BUS_HOST=seleniumHub
                  - SE_EVENT_BUS_PUBLISH_PORT=4442
                  - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
                  - SE_NODE_MAX_SESSIONS=2
            depends_on:
                  - HubService
```

Powershell
```
# In Powershell

docker compose up
# to run the docker-compose.yml file
```

> The containers for hub and nodes will be created with specific port and nodes will be associated to the seleniumHub.
> The grid console can be viewed by accessing "http://localhost:4445/ui/index.html".
> The nodes linked to the hub can also be viewed in the Grid Console.

## Run Testcases from Eclipse

- Goto Eclipse 
- Open "amazon.xml" testng testsuite file.
- Run as TestNG suite

> The test cases will be executed in background and the sessions can be viewed in "http://localhost:4445/ui/index.html".




##### Java Code

###### AmazonPageObjects.java

```java
  package com.amazon.container.pages;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class AmazonPageObjects
{
	WebDriver driver;
     //constructor
	
	public AmazonPageObjects(WebDriver driver)
	{
		this.driver=driver;
	}

	//locators or page objects
    By hamburger = By.linkText("All");
    
    By tv_app = By.partialLinkText("TV, Appliances");
    
    By television = By.partialLinkText("Televisions");
    
    By scroll_to_brands = By.xpath( "//span[text()='Brands']");
    
    By samsung_checkbox = By.xpath("//span[text()='Samsung']");
    
    By sort = By.xpath("/html/body/div[1]/div[2]/span/div/h1/div/div[2]/div/div/form/span/span/span/span/span[2]");
    
    By price = By.partialLinkText("Price: High");

    By img = By.className("s-image");
    
    By scroll_to_about = By.xpath("//div[@id='feature-bullets']");
    
    By about = By.xpath("//div[@id='feature-bullets']/h1");
    

   //methods
	public void launchApp()
    {
    	driver.get("https://www.amazon.in");
        System.out.println("Browser launched and navigated to Amazon");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }
    
    public void clickHamburgerMenu() throws InterruptedException
    {
    	driver.findElement(hamburger).click();
    	Thread.sleep(5000);
    	
    }
    
    public void selectCategory() throws InterruptedException
    {
    
        driver.findElement(tv_app).click();
    	Thread.sleep(3000);
        
    }
    
    public void clickTelevision() throws InterruptedException  
    {
    	driver.findElement(television).click();
    	Thread.sleep(3000);
    	
    }
    
    public void applyFilter() throws InterruptedException
    {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", driver.findElement(scroll_to_brands));
        driver.findElement(samsung_checkbox).click();
        Thread.sleep(3000);
    }
    
    public void clickSecHighestItem() throws InterruptedException
    {
    	driver.findElement(sort).click();
    	//driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    	Thread.sleep(3000);
    	driver.findElement(price).click();
    	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        List<WebElement> img_links = driver.findElements(img);
        img_links.get(1).click();
    }

    public boolean isAboutThisItemPresent()
    {
		
		//Get current window handle
        String mainWindowHandle = driver.getWindowHandle();
        //get all window handles
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> I1= allWindowHandles.iterator();
        while(I1.hasNext())
        {

        String new_window=I1.next();
        if(!mainWindowHandle.equals(new_window))
        {
            //switch to next tab/window
        driver.switchTo().window(new_window);
        JavascriptExecutor js1 = (JavascriptExecutor) driver;
        js1.executeScript("arguments[0].scrollIntoView();", driver.findElement(scroll_to_about));
        String aboutthis = driver.findElement(about).getText();
        if(aboutthis.equals("About this item"))
        {
            System.out.println(aboutthis);
        	return true;
        }
       
        }
}
		return false;
}
    }
```

###### AmazonTest.java

```java
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


```
## TestSuite xml file

###### amazom.xml

``xml
 <!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="Grid Test Suite" parallel="tests">
	<test name="Amazon TestCase on Firefox">
	<parameter name="browser" value="firefox"></parameter>
		<classes>
			<class name="com.amazon.container.test.AmazonTest"></class>
		</classes>
	</test>
	
	<test name="Amazon TestCase on Chrome">
	<parameter name="browser" value="chrome"></parameter>
		<classes>
			<class name="com.amazon.container.test.AmazonTest"></class>
		</classes>
	</test>	
	
</suite>
```

## Conclusion
Test case is successfully executed in Chrome and Firefox using Selenium Grid 4 using Dockerization concept. 

