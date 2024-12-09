package prac;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;

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
        String targetUsers = "specific"; // all -> all users || specific -> specific users || other -> other options
        String pushTopic = "SYSTEM"; // SYSTEM || NEWS || PRICE
        String redirectionPlace = "screen"; // url -> to send on web || screen -> to send in mobile
        String url = "https://www.ndtv.com/india-news/maharashtra-oath-ceremony-live-updates-devendra-fadnavis-to-be-sworn-in-as-maharashtra-chief-minister-pm-modi-to-attend-7176368";
        String redirectScreen = "wallet";  // home, market, spot, derivatives, wallet
        String[] emails = {"copt1@yopmail.com", "pn2@yopmail.com"};

        int attempts = 3;
        boolean flag = false;

        Integer count = 1;

        driver.get(adminUrl);
        performAdminLogin();

        // Go to notifications page
        Thread.sleep(1000);
        driver.get("https://qa-admin.bitdelta.com/notification");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()='Notification']")));

        // Perform push/email notifications
        Scanner sc = new Scanner(System.in);

        while (attempts >= count) {
//        while (flag) {
            pushNotification(notificationType, targetUsers, pushTopic, redirectionPlace, redirectScreen, emails, url, count);
            Thread.sleep(500);
            count++;

            if (flag) {
                System.out.println("Do you want to send the same notification again? y/n: ");
                String choice = sc.nextLine();

                if (choice.equals("n")) {
                    flag = false;
                }
                return;
            }
        }
    }

    private void pushNotification(String notificationType, String targetUsers, String pushTopic, String redirectionPlace, String redirectScreen, String[] emails, String url, Integer count) throws InterruptedException {
        driver.findElement(By.xpath("(//select)[1]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + notificationType + "']"))).click();

        driver.findElement(By.xpath("(//select)[2]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + targetUsers + "']"))).click();

        if (targetUsers.equals("all")) {
            driver.findElement(By.xpath("(//select)[3]")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + pushTopic + "']"))).click();
        } else if (targetUsers.equals("specific")) {
            WebElement emailBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='border-t border-gray-200']//div[3]//textarea[1]")));
            emailBox.click();

            for (String email : emails) {
                emailBox.sendKeys(email + ",");
                Thread.sleep(300);
            }
        }

        // Enter title
        String title;
        WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter title here']")));

        if ("specific".equals(targetUsers) && (count == null || count == 0)) {
            title = "SPECIFIC: Test title for specific users";
        } else if ("specific".equals(targetUsers)) {
            title = "SPECIFIC: Test title " + count + " for specific users";
        } else if ("all".equals(targetUsers) && count != null && count > 0) {
            title = pushTopic + " - " + redirectScreen.toUpperCase() + " Screen " + count + ": Test title for all users";
        } else {
            title = pushTopic + " - " + redirectScreen.toUpperCase() + " Screen: Test title for all users";
        }
        inputField.sendKeys(title);


        // Enter body
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Enter content here...']")))
                .sendKeys(pushTopic + ": Test content/body for " + redirectScreen.toUpperCase() + " for " + targetUsers + " users.");

        // select Redirection place
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[normalize-space()='Redirection Place']/following-sibling::*[1]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//option[@value='" + redirectionPlace + "']"))).click();

        if (redirectionPlace.equals("url")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter a valid URL']")))
                    .sendKeys(url);
        }

        if (redirectionPlace.equals("screen")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[normalize-space()='Redirect Screen']/following-sibling::*[1]"))).click();
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
