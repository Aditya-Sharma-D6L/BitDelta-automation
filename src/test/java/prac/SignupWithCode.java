package prac;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignupWithCode {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("<<<<<<<<<<<<<< Registration initiated >>>>>>>>>>>>>>");


        // Set the ChromeDriver property (uncomment if needed)
        // System.setProperty("webdriver.chrome.driver", "/Users/techteam45/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        String email = "testuser01@yopmail.com";
        String password = "Pass@12345";
        String referralCode = "r5lhsz_A_xlnpq1";

        // Define the base URL
        String baseUrl = "https://staging.bitdelta.com/en/";

        // Access the base URL
        driver.get(baseUrl);

        // Navigate to the register page
        driver.get(baseUrl + "register");

        // select country
//        WebElement country = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'css-lez8p9')]")));
//        country.click();
//        country.sendKeys("India");

        // Email field
        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(email);

        // Password field
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(password);

        //fill the referral code field
            // click the referral code dropdown
        driver.findElement(By.xpath("//button[@id='accordion-button-:R4udlal6pkd5t6dja:']//*[name()='svg']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//input[@id='field-:R1atcudlal6pkd5t6dja:']")).sendKeys(referralCode);

        // click register button
        WebElement registerButton = driver.findElement(By.xpath("//button[@class='chakra-button css-1bkdqyd']"));
        registerButton.click();

        // Look for TnC popup
        try {
            Thread.sleep(2000);
            driver.findElement(By.xpath("//span[@class='chakra-checkbox__control css-1osrtzr']")).click();

            WebElement scrollButton = driver.findElement(By.xpath("//div[contains(text(),'Scroll Down')]"));
            scrollButton.click();

            Thread.sleep(2000);

            WebElement acceptButton = driver.findElement(By.xpath("//button[normalize-space()='Agree']"));
            acceptButton.click();
        } catch (NoSuchElementException e) {
            System.out.println("TnC popup not found. Continuing without accepting.");
            // You can choose to log the exception or handle it further if needed
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Handle OTP Input
        handleOTP(driver);

        // Initialize GeneralSurvey and fill out the questionnaire
        GeneralSurvey survey = new GeneralSurvey(driver);
        survey.fillSurvey();
        survey.fillAdditionalQuestions();

        // Close the browser after execution
        System.out.println("<<<<<<<<<<<<<< Registration completed >>>>>>>>>>>>>>");

    }

    private static void handleOTP(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Wait for the OTP fields and fill them
            for (int i = 0; i < 6; i++) {
//                WebElement pinInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id='pin-input-:r9:-" + i + "']")));
                //input[@id='pin-input-:r9:-0']
                WebElement pinInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@data-index='" + i + "']")));
                pinInputField.sendKeys(String.valueOf(i + 1)); // Sample OTP value for demo purposes
            }
        } catch (Exception e) {
            System.out.println("Error while entering OTP: " + e.getMessage());
        }
    }
}
