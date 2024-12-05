package prac;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;

public class EmailAndPushNotifications {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String env = "qa";

    public EmailAndPushNotifications() {
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void performPushNotification() throws InterruptedException, IOException {
        String adminUrl = "https://" + env + "-admin.bitdelta.com/login";
        driver.manage().window().maximize();

        String notificationType = "notification"; // notification -> push notification || email -> email notification
        String targetUsers = "all"; // all -> all users || specific -> specific users || other -> other options
        String pushTopic = "NEWS"; // SYSTEM || NEWS || PRICE
        String redirectionPlace = "screen"; // url -> to send on web || screen -> to send in mobile
        String redirectScreen = "market";  // home, market, spot, derivatives, wallet

        driver.get(adminUrl);
        performAdminLogin();

        // Go to notifications page
        Thread.sleep(1000);
        driver.get("https://qa-admin.bitdelta.com/notification");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()='Notification']")));

        // Perform push/email notifications
        pushNotification(notificationType, targetUsers, pushTopic, redirectionPlace, redirectScreen);
    }

    private void pushNotification(String notificationType, String targetUsers, String pushTopic, String redirectionPlace, String redirectScreen) {
        driver.findElement(By.xpath("(//select)[1]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + notificationType + "']"))).click();

        driver.findElement(By.xpath("(//select)[2]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + targetUsers + "']"))).click();

        driver.findElement(By.xpath("(//select)[3]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + pushTopic + "']"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter title here']"))).sendKeys("News: Test title for " + targetUsers + " users");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Enter content here...']")))
                .sendKeys("News: Test content/body for " + targetUsers + " users. Lorem Ipsum...");

        driver.findElement(By.xpath("(//select)[4]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + redirectionPlace + "']"))).click();

        if (redirectionPlace.equals("url")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter a valid URL']")))
                    .sendKeys("https://qa.bitdelta.com/en/trade/spot/btc-usdt");
        }

        if (redirectionPlace.equals("screen")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//select)[5]"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + redirectScreen + "']"))).click();
        }

        driver.findElement(By.xpath("//button[text()='Send notification']")).click();
    }

    private void performAdminLogin() throws InterruptedException {
        String adminEmail;
        String adminPassword;

        if (env.equals("staging")) {
            adminEmail = "ashutosh.parihar@delta6labs.com";
            adminPassword = "Pass@12345";
        } else {
            adminEmail = "laxman.kumar@bitdelta.com";
            adminPassword = "Pass@1234567";
        }

        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(adminEmail);
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(adminPassword);
        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        handleTwoFa();
    }

    private void handleTwoFa() throws InterruptedException {
        Thread.sleep(2000);
        for (int i = 1; i <= 6; i++) {
            WebElement inputField = driver.findElement(By.xpath("(//input[@class='border rounded w-10 h-10 text-center form-control'])[" + i + "]"));
            inputField.sendKeys(String.valueOf(i)); // Example 2FA code for testing
        }

        driver.findElement(By.xpath("//button[contains(text(), 'Submit')]")).click();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        EmailAndPushNotifications app = new EmailAndPushNotifications();
        app.performPushNotification();
    }
}