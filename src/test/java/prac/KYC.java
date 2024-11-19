package prac;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class KYC {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public KYC(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void approveKYC() throws InterruptedException {

        // click on "Dashboard"
        driver.findElement(By.xpath("//nav[contains(@class, 'top-nav bg-theme-1 block')]//div[contains(text(), 'Dashboard ')]")).click();

        Thread.sleep(2000);
        // Click on "KYC Actions"
        driver.findElement(By.xpath("//div[normalize-space()='Kyc Actions']")).click();

        // Toggle on "Info Kyc"
        driver.findElement(By.xpath("//div[@class='flex flex-wrap gap-4 mt-5']//div[1]//input[1]")).click();

        // Toggle on "Basic Kyc"
        driver.findElement(By.xpath("//div[@class='content']//div[2]//input[1]")).click();

        // Enter a random comment in the comment field
        Thread.sleep(1000);
        driver.findElement(By.xpath("//textarea[@placeholder='Add a comment']")).sendKeys("KYC Approved");

        // Click on "Submit Changes" button
        driver.findElement(By.xpath("//button[@class='btn btn-primary mt-4']")).click();

        // Handle Alert popup
        Alert alert = driver.switchTo().alert();
        alert.accept();

        System.out.println("KYC Approved successfully.");
    }
}
