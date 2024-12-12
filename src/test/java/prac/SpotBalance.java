package prac;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SpotBalance {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public SpotBalance(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void updateSpotBalance(String spotBalance) throws InterruptedException {

        Thread.sleep(1000);
        // Click on "Reports" dropdown
        driver.findElement(By.xpath("//div[normalize-space()='Reports']")).click();

        Thread.sleep(500);
        // Click on "Spot Balance"
        driver.findElement(By.xpath("//div[normalize-space()='Spot Balance']")).click();

        // Search for "USDT"
//        driver.findElement(By.xpath("//input[@placeholder='search...']")).sendKeys("USDT");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='search...']"))).sendKeys("USDT");
        Thread.sleep(1500);

        // Click on "Edit" button to enter amount
        driver.findElement(By.xpath("//table[@class='table table-report mt-2']//a[@class='flex items-center mr-3 cursor-pointer'][normalize-space()='Edit']")).click();

        // Locate the input field
        Thread.sleep(2000);
        WebElement amountField = driver.findElement(By.xpath("//input[@placeholder='amount']"));
        amountField.clear();
        Thread.sleep(1000);
        amountField.sendKeys(spotBalance);

        // Enter 2FA code
        driver.findElement(By.xpath("//input[@placeholder='twofacode']")).sendKeys("123456");

        // Click on "Update" button
        driver.findElement(By.xpath("//div[@id='editCurrencyModal']//button[@type='button'][normalize-space()='Update']")).click();

        // click on "Dashboard"
        WebElement dashboard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div//span[text()='Dashboard']")));
        dashboard.click();

        // click on "Profile Overview"
        driver.findElement(By.xpath("//div[normalize-space()='Profile Overview']")).click();

        System.out.println("Spot balance updated successfully.");
    }
}
