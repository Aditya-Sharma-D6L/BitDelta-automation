package prac;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import prac.copyTrading.*;

import java.time.Duration;

public class Login {

    private static final String BASE_URL = "https://copy-trading.bitdelta.com/en/";
    private static final String LOGIN_URL = BASE_URL + "login";

    private static final String EMAIL = "bctmaster40@yopmail.com";
    private static final String PASSWORD = "Pass@12345";
    private static final Boolean applyForMasterTrader = true;
    private static final Boolean transferFromSpotToDerivatives = true;

    private final WebDriverWait wait;
    private final WebDriver driver;

    public Login() {
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    public static void main(String[] args) throws InterruptedException {
        Login login = new Login();
        try {
            login.performLogin();
        } finally {
            login.teardown();
        }
    }

    public void performLogin() throws InterruptedException {
        System.out.println("Login initiated");

        driver.get(LOGIN_URL);

        // enable dark theme
        WebElement toggleTheme = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Toggle theme mode']//*[name()='svg']")));
        toggleTheme.click();

        enterCredentials();
        clickLoginButton();
        handleTnCPopup();

        Thread.sleep(2000);
        handleLoginOTP();

        Thread.sleep(1500);
        fillGeneralSurvey();

        // transfer amount in derivatives wallet
        if (transferFromSpotToDerivatives) {
            transferAmount();
        }

        // go to derivatives and place hedging order
        Thread.sleep(1000);
        placeDerivativeOrder();

        // wait sometime after derivative order is closed
        Thread.sleep(2000);

        // apply for master trader
        if (applyForMasterTrader) {
            System.out.println("Going to apply for master trader");
            applyMasterTrader();
        } else {
            System.out.println("Not applying for master trader");
        }

    }

    public void transferAmount() throws InterruptedException {
        TransferFromSpotToDerivatives transfer = new TransferFromSpotToDerivatives(driver);
        transfer.transferFromSpotToDerivatives();
    }

    private void enterCredentials() {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']")));
        emailField.sendKeys(EMAIL);

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Password']")));
        passwordField.sendKeys(PASSWORD);
    }

    private void clickLoginButton() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Log In')]")));
        loginButton.click();
    }

    private void handleTnCPopup() {
        try {
            WebElement tncCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='chakra-checkbox__control css-1osrtzr']")));
            tncCheckbox.click();

            WebElement acceptButton = driver.findElement(By.xpath("//button[normalize-space()='Agree']"));
            acceptButton.click();
        } catch (Exception e) {
            System.out.println("TnC not found or already accepted. Continuing...");
        }
    }

    private void handleLoginOTP() {
        try {
            for (int i = 0; i < 6; i++) {
                WebElement pinInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@data-index='" + i + "']")));
//                WebElement pinInputField = driver.findElement(By.xpath("//input[@id='pin-input-:r1:-'" + i + "']"));
                pinInputField.sendKeys(String.valueOf(i + 1));  // Sample OTP entry for demonstration
            }
        } catch (Exception e) {
            System.out.println("Error while entering OTP: " + e.getMessage());
        }
    }

    private void fillGeneralSurvey() {
        try {
            GeneralSurvey survey = new GeneralSurvey(driver);
            survey.fillSurvey();
            survey.fillAdditionalQuestions();
            System.out.println("General Survey Completed.");
        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("Survey already filled, continuing...");
        } catch (Exception e) {
            System.out.println("An error occurred while attempting to fill out the survey or survey is already filled.");
        }
    }

    public void applyMasterTrader() {
        try {

            ApplyMasterTrader apply = new ApplyMasterTrader(driver);
            apply.goToCopyTrading();

        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("Error filling the form: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while attempting to click copy trading button.");
            System.out.println(e.getMessage());
        }
    }

    public void placeDerivativeOrder() {
        try {
            PlaceDerivativesOrder placeOrder = new PlaceDerivativesOrder(driver);
            placeOrder.goToDerivatives();

            try {
                placeOrder.completeDerivativesQuiz();
            } catch (Exception e) {
                System.out.println("Seems like Derivatives Quiz is already done.");
            }

            placeOrder.placeMarketOrder();
        } catch (Exception e) {
            System.out.println("Error placing derivatives market order.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void teardown() {
        if (driver != null) {
//            driver.quit();
            System.out.println("Login process completed");
        }
    }
}
