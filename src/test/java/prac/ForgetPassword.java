package prac;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ForgetPassword {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Forget Password Initiated");

        WebDriver driver = new ChromeDriver();

        String baseUrl = "https://staging.bitdelta.com/en/";

        String email = "bitdeltauser@yopmail.com";
        String password = "Pass@123456";

        driver.get(baseUrl + "forgot-password");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement enterEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']")));
        enterEmail.sendKeys(email);

        // click "Submit"
        Thread.sleep(2000);
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();

        // enter OTP
        handleOTP(driver, email);
    }

    private static void handleOTP(WebDriver driver, String email) {
        try {

            ((JavascriptExecutor) driver).executeScript("window.open()");

            // Get a list of all window handles (tabs)
            ArrayList<String> browserTabs = new ArrayList<>(driver.getWindowHandles());

            // Switch to the newly opened tab
            driver.switchTo().window(browserTabs.get(1));

            // visit email provider website to get OTP from email
            driver.get("https://yopmail.com/en/");

            // enter email
            driver.findElement(By.xpath("//input[@placeholder='Enter your inbox here']")).sendKeys(email);

            // click "Arrow" after entering email
            Thread.sleep(1000);
            driver.findElement(By.xpath("//button[@title='Check Inbox @yopmail.com']")).click();

            // click and open the "Forgot Password" mail in the inbox

            // Wait for the iframe to be available and switch to it
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Step 1: Switch to the first iframe where the "Forgot Password" email is located
            WebElement iframeElementMail = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ifinbox")));
            driver.switchTo().frame(iframeElementMail);

            // Locate and click the "Forgot Password" email
            WebElement forgotPasswordEmail = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='lms' and contains(text(),'[BitDelta Exchange] Forgot Password')]")));
            forgotPasswordEmail.click();
            System.out.println("Forgot password email clicked.");

            // Step 2: Switch back to default content after interacting with the first iframe
            driver.switchTo().defaultContent();

            // Step 3: Wait and switch to the second iframe to locate the OTP
            Thread.sleep(2000);  // Optional, for ensuring the content has time to load
            System.out.println("Looking for OTP iframe...");

            // Ensure you're in the correct iframe for the OTP
            WebElement iframeElementOTP = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ifmail")));
            driver.switchTo().frame(iframeElementOTP);

            // Locate the OTP element
            WebElement searchOTP = driver.findElement(By.xpath("//p[contains(text(), 'Your verification code is')]//following::p[string-length(normalize-space(text()))=6]\n"));
            System.out.println("OTP iframe element found");

            // Get the OTP text
            String fullText = searchOTP.getText();
            System.out.println("OTP is: " + fullText);
            String OTP = fullText.replace("Your verification code is", "").trim(); // Extract just the OTP

            // Step 4: Break OTP into individual characters and store in a list
            List<String> otpList = new ArrayList<>();
            for (char c : OTP.toCharArray()) {
                otpList.add(String.valueOf(c));
            }

            // Print the individual OTP values
            System.out.println("OTP broken into individual values: " + otpList);

            // Switch back to 1st opened tab
            driver.switchTo().window(browserTabs.getFirst());

            // wait for user to do CAPTCHA and browser to load OTP page
            Thread.sleep(5000);

            // fill the OTP
            //input[@id='pin-input-:r3:-0']
            for (int i = 0; i < 6; i++) {
                WebElement otpInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='pin-input-:r3:-" + i + "']")));
                otpInputField.sendKeys(otpList.get(i));
            }


        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
