package prac;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import prac.Derivatives.PlaceDerivativesOrder;
import prac.Derivatives.PlaceSpotOrder;
import prac.copyTrading.*;

import java.time.Duration;

public class Login {

    // GENERAL DETAILS AND CREDENTIALS
    private static final String EMAIL = "pagesort12@yopmail.com";
    private static final String PASSWORD = "Pass@12345";
    public static final String ENV = "staging";

    // COPY TRADING DETAILS
    private static final boolean applyForMasterTrader = false;
    private static final boolean transferFromSpotToDerivatives = false;

    // SPOT ORDER DETAILS
    private static final boolean placeSpotOrders = false;
    private static final String SELL = "sell";
    private static final String BUY = "buy";
    private static final int spotOrderCount = 5;
    private static final String spotOrderType = BUY;
    private static final String amount = "15"; // This is the $ amount sent to the input fields in the market order for buy/sell

    // DERIVATIVES ORDER DETAILS
    private static final boolean placeDerivativesOrders = true;
    private static final String DSELL = "sell";
    private static final String DBUY = "buy";
    private static final int derivativesOrderCount = 20;
    private static final String derivativesOrderType = DBUY;

    private static final String BASE_URL = "https://" + ENV + ".bitdelta.com/en/";
    private static final String LOGIN_URL = BASE_URL + "login";

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
//        clickLoginButton();

        handleTnCPopup();

        Thread.sleep(1000);
        handleLoginOTP();

        Thread.sleep(1500);
        fillGeneralSurvey();

        // apply for master trader
        boolean checkHedgingOrderWhenApplyingForMaster = false;
        if (applyForMasterTrader) {

            checkHedgingOrderWhenApplyingForMaster = true;

            // transfer amount in derivatives wallet
            if (transferFromSpotToDerivatives) {
                transferAmount();
            }


            // go to derivatives and place hedging order
            Thread.sleep(1000);
            placeDerivativeOrder(ENV, checkHedgingOrderWhenApplyingForMaster);

            // wait sometime after derivative order is closed
            Thread.sleep(2000);

            System.out.println();
            System.out.println("Going to apply for master trader");
            applyMasterTrader();
        } else {
            System.out.println("Not applying for master trader for this user");
        }

        // Place spot orders
        if (placeSpotOrders) {
            placeSpotOrders();
        }

        if (placeDerivativesOrders) {
            placeDerivativeOrder(ENV, checkHedgingOrderWhenApplyingForMaster);
        }
    }

    private void placeSpotOrders() throws InterruptedException {
        PlaceSpotOrder p = new PlaceSpotOrder(driver);

        // go to spot
        p.goToSpotPage();

        p.placeBuyMarketOrders(spotOrderCount, spotOrderType, amount);

    }

    public void transferAmount() throws InterruptedException {
        TransferFromSpotToDerivatives transfer = new TransferFromSpotToDerivatives(driver);
        transfer.transferFromSpotToDerivatives();
    }

    private void enterCredentials() {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Email']")));
        emailField.sendKeys(EMAIL);

        clickLoginButton();

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Password']")));
        passwordField.sendKeys(PASSWORD);

        // click "Next" button
        driver.findElement(By.xpath("//button[@type='submit' and text()='NEXT']")).click();
    }

    private void clickLoginButton() {
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Log In')]")));
        loginButton.click();
    }

    private void handleTnCPopup() {
        try {
            try {
//                WebElement scrollButton = driver.findElement(By.xpath("//div[contains(text(),'Scroll Down')]"));
                WebElement scrollButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Scroll Down')]")));

                // Ensure the element is scrolled into view before clicking
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", scrollButton);

                scrollButton.click();
            } catch (NoSuchElementException | TimeoutException e) {

                Thread.sleep(1000);

                WebElement checkBox = driver.findElement(By.xpath("//label/span/p[text()='I agree to the BitDelta Terms and conditions']"));
                checkBox.click();

                WebElement acceptButton = driver.findElement(By.xpath("//button[normalize-space()='Agree']"));
                acceptButton.click();
            }
        } catch (Exception e) {
            System.out.println("TnC not found, continuing...");
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
            System.out.println("General Survey already filled, continuing...");
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
            System.out.println("An error occurred while attempting to click 'Start Copy Trading' button.");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void placeDerivativeOrder(String env, boolean status) {
        try {
            PlaceDerivativesOrder placeOrder = new PlaceDerivativesOrder(driver);
            placeOrder.goToDerivatives();

            try {
                placeOrder.completeDerivativesQuiz();
            } catch (Exception e) {
                System.out.println("Derivatives quiz already done");
            }

            placeOrder.placeMarketOrder(derivativesOrderCount, derivativesOrderType, env, status);
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
