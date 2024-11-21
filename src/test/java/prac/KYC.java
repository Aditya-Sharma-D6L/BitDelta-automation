package prac;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class KYC {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public KYC(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private void processInfoKycOnQA(WebDriver driver) throws InterruptedException {

        // Toggle on "Info Kyc"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='flex flex-wrap gap-4 mt-5']//div[1]//input[1]"))).click();

        // Enter a random comment in the comment field
        Thread.sleep(300);
        driver.findElement(By.xpath("//textarea[@placeholder='Add a comment']")).sendKeys("KYC Approved");

        // Click on "Submit Changes" button
        driver.findElement(By.xpath("//button[@class='btn btn-primary mt-4']")).click();

        // Handle Alert popup
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    private void processBasicKycOnQA(WebDriver driver) throws InterruptedException {
        // Toggle on "Basic Kyc"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content']//div[2]//input[1]"))).click();

        // Enter a random comment in the comment field
        Thread.sleep(300);
        driver.findElement(By.xpath("//textarea[@placeholder='Add a comment']")).sendKeys("KYC Approved");

        // Click on "Submit Changes" button
        driver.findElement(By.xpath("//button[@class='btn btn-primary mt-4']")).click();

        // Handle Alert popup
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void approveKYC(String env) throws InterruptedException {

        // click on "Dashboard"
        WebElement dashboard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='top-menu__title'][normalize-space()='Dashboard']")));
        dashboard.click();

        // Click on "KYC Actions"
        driver.findElement(By.xpath("//div[normalize-space()='Kyc Actions']")).click();

        Thread.sleep(1500);

        // when env is QA
        if (env.equals("qa")) {

            processInfoKycOnQA(driver);
            Thread.sleep(500);
            processBasicKycOnQA(driver);

            System.out.println("KYC Approved successfully.");
            return;
        }

        // when env is Staging
        // Toggle on "Info Kyc"
//        driver.findElement(By.xpath("//div[@class='flex flex-wrap gap-4 mt-5']//div[1]//input[1]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='flex flex-wrap gap-4 mt-5']//div[1]//input[1]"))).click();

        Thread.sleep(500);

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
