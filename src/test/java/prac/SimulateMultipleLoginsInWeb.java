package prac;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SimulateMultipleLoginsInWeb {

    private static final String EMAIL = "bctmaster@yopmail.com";
    private static final String PASSWORD = "Pass@12345";
    public static final String ENV = "qa";
    private static final String BASE_URL = "https://" + ENV + ".bitdelta.com/en/";
    private static final String LOGIN_URL = BASE_URL + "login";

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor initializes WebDriver and WebDriverWait
    public SimulateMultipleLoginsInWeb() {
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    // Handle the general survey filling process
    private void fillGeneralSurvey() {
        try {
            GeneralSurvey survey = new GeneralSurvey(driver);
            survey.fillSurvey();
            survey.fillAdditionalQuestions();
            System.out.println("General Survey Completed.");
        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("General Survey already filled");
        } catch (Exception e) {
            System.out.println("An error occurred while attempting to fill out the survey.");
        }
    }

    // Handle Terms and Conditions popup
    private void handleTnCPopup() {
        try {
            WebElement scrollButton = new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Scroll Down')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", scrollButton);
            scrollButton.click();

            WebElement checkBox = driver.findElement(By.xpath("//label/span/p[text()='I agree to the BitDelta Terms and conditions']"));
            checkBox.click();

            WebElement acceptButton = driver.findElement(By.xpath("//button[normalize-space()='Agree']"));
            acceptButton.click();

        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("TnC popup not found or already handled.");
        } catch (Exception e) {
            System.out.println("Error while handling TnC popup.");
        }
    }

    // Handle OTP input for login
    private void handleLoginOTP() {
        try {
            for (int i = 0; i < 6; i++) {
                WebElement pinInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@data-index='" + i + "']")));
                pinInputField.sendKeys(String.valueOf(i + 1));  // Sample OTP entry for demonstration
            }
        } catch (Exception e) {
            System.out.println("Error while entering OTP: " + e.getMessage());
        }
    }

    // Click the Login button
    private void clickLoginButton() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Log In')]")));
        loginButton.click();
    }

    // Enter login credentials and submit
    private void enterCredentials(String email) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']")));
        emailField.sendKeys(email);

        clickLoginButton();

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Password']")));
        passwordField.sendKeys(PASSWORD);

        // click "Next" button
        driver.findElement(By.xpath("//button[@type='submit' and text()='NEXT']")).click();
    }

    // Cleanup and close the browser
    private void teardown() {
        if (driver != null) {
            driver.quit();  // Close the current browser instance
        }
    }

    // Perform login, processing the email for each attempt
    private void performLogin(String email, int attempts) throws InterruptedException {
        System.out.println("Login initiated");

        driver.get(LOGIN_URL);

        // Enable dark theme (Optional)
        WebElement toggleTheme = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Toggle theme mode']//*[name()='svg']")));
        toggleTheme.click();

        String newEmail = processEmail(email, attempts);
        enterCredentials(newEmail);

        handleTnCPopup();


        // Wait before entering OTP and handling survey
        Thread.sleep(1000);
        handleLoginOTP();

        Thread.sleep(1500);
        fillGeneralSurvey();
    }

    // Process the email and append the attempt number (e.g. pn+1@yopmail.com)
    private static String processEmail(String email, int attempts) {
        // Split the email at '@'
        String[] parts = email.split("@");

        // Check if there is a valid split
        if (parts.length == 2) {
            String localPart = parts[0]; // part before '@'
            String domainPart = parts[1]; // part after '@'

            // Concatenate attempt number with '+' before the domain
            return localPart + attempts + "@" + domainPart;
        } else {
            return "Invalid email format";
        }
    }

    // Main method to simulate multiple login attempts
    public void login() throws InterruptedException {
        int attempts = 40;
        int maxAttempts = 42;

        while (attempts <= maxAttempts) {
            // Perform login for the current attempt
            performLogin(EMAIL, attempts);
            System.out.println("Login attempt " + attempts + " done");

            attempts++;


            // Close the browser after each login attempt and start a new one
            teardown();

            // Re-initialize the WebDriver for the next attempt
            this.driver = new ChromeDriver();
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            driver.manage().window().maximize();
        }
    }

    // Main execution point
    public static void main(String[] args) throws InterruptedException {
        SimulateMultipleLoginsInWeb smlog = new SimulateMultipleLoginsInWeb();
        smlog.login();
    }
}
