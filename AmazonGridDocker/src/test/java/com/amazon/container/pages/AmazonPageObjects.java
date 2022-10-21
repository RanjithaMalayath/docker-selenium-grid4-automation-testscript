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