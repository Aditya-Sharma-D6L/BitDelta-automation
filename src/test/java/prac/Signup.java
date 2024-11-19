package prac;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class Signup {

    private static final String REGISTER_URL = "https://staging.bitdelta.com/en/register";
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Control variable for initiating KYC and Spot Balance
    protected boolean initiateKYCAndSpot = true;  // Set to true by default

    public Signup() {
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public static void main(String[] args) throws InterruptedException {
        Signup signup = new Signup();

        String email = "bctmaster40@yopmail.com";
        String password = "Pass@12345";
        String spotBalance = "3000";

        try {
            signup.registerUser(email, password, spotBalance);
        } finally {
            signup.teardown();
        }
    }

    public void registerUser(String email, String password, String spotBalanceAmount) throws InterruptedException {
        driver.get(REGISTER_URL);
        driver.manage().window().maximize();

        Thread.sleep(2000);

        // Enable dark theme
        WebElement toggleTheme = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Toggle theme mode']//*[name()='svg']")));
        toggleTheme.click();
        Thread.sleep(1000);

        enterCredentials(email, password);
        submitRegistrationForm();

        Thread.sleep(1000);
        handleTnCPopup();

        Thread.sleep(2000);
        handleOTP();

        Thread.sleep(2000);
        fillGeneralSurvey();

        Thread.sleep(2000);

        // Execute KYC and Spot Balance initiation based on the control variable
        if (initiateKYCAndSpot) {
            initiateKYCAndSpotBalance(email, spotBalanceAmount);
        }
    }

    private void enterCredentials(String email, String password) {
        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(password);
    }

    private void submitRegistrationForm() {
        WebElement registerButton = driver.findElement(By.xpath("(//button[@type='button' and text()='Register'])[2]"));
        registerButton.click();
    }

    private void handleTnCPopup() {
        try {
//            WebElement scrollButton = driver.findElement(By.xpath("//div[contains(text(),'Scroll Down')]"));
            WebElement scrollButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Scroll Down')]")));

            // Ensure the element is scrolled into view before clicking
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", scrollButton);

            scrollButton.click();
            Thread.sleep(1000);

            WebElement checkBox = driver.findElement(By.xpath("//label/span/p[text()='I agree to the BitDelta Terms and conditions']"));
            checkBox.click();

            WebElement acceptButton = driver.findElement(By.xpath("//button[normalize-space()='Agree']"));
            acceptButton.click();
        } catch (Exception e) {
            System.out.println("TnC popup not found or could not be handled.");
        }
    }

    private void handleOTP() throws InterruptedException {
        Thread.sleep(1000);
        for (int i = 0; i < 6; i++) {
            WebElement pinInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@data-index='" + i + "']")));
            pinInputField.sendKeys(String.valueOf(i + 1)); // Sample OTP
        }

        Thread.sleep(1500); // Brief pause to ensure OTP is fully entered

        boolean clicked = false;
        int attempts = 0;
        while (!clicked && attempts < 3) {
            try {
                driver.findElement(By.xpath("//button[@type='button' and text()='Verify']")).click();
                clicked = true;
            } catch (NoSuchElementException e) {
                attempts++;
                Thread.sleep(500); // Adjust wait time as needed
            }
        }
    }

    private void fillGeneralSurvey() {
        GeneralSurvey generalSurvey = new GeneralSurvey(driver);
        generalSurvey.fillSurvey();
        generalSurvey.fillAdditionalQuestions();
    }

    private void initiateKYCAndSpotBalance(String email, String spotBalanceAmount) {
        KycAndSpot kycAndSpot = new KycAndSpot(driver);
        try {
            kycAndSpot.initiateKycAndSpotBalance(null, email, spotBalanceAmount, true); // true to open in a new tab
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void teardown() {
        if (driver != null) {
//            driver.quit();
            System.out.println("Everything done, no error occurred.");
        }
    }
}