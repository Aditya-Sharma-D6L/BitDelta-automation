package prac.copyTrading;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.MessageFormat;
import java.time.Duration;

public class PlaceDerivativesOrder {

    public WebDriver driver;
    public WebDriverWait wait;

    public PlaceDerivativesOrder(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    public void goToDerivatives() throws InterruptedException {
        WebElement clickTradeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/p[text()='Trade']")));
        clickTradeButton.click();

        WebElement goToDerivativesPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='navigation-bar-item-menu undefined']//p[normalize-space()='Derivatives']")));
        goToDerivativesPage.click();
        System.out.println("Going to derivatives page...");
        Thread.sleep(2000);
    }

    public void completeDerivativesQuiz() throws InterruptedException {
        DerivativesQuiz quiz = new DerivativesQuiz(driver);
        quiz.performDerivativesQuiz(driver);
    }

    private boolean checkIfAlreadyOrderPlacedInHedgingMode () {
        boolean flag = true;

        System.out.println("Looking for old hedging order records...");
        // go to the "Trade History" tab and look for hedging orders
        WebElement tradeHistory = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Trade History']")));
        tradeHistory.click();

        try {
            // check if closed hedging orders exists or not
            WebElement lookForHedgingOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='tabs-:r11:--tabpanel-3']/div//div[text()='HEDGING']")));

        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("No Hedging order(s) found");
            flag = false;
        } catch (Exception e) {
            System.out.println("Error occurred while looking for the 'HEDGING' element: " + e.getMessage());
        }

        return flag;
    }

    public void placeMarketOrder() throws InterruptedException {

        // check if user has already placed a market order in Hedging mode
        if (checkIfAlreadyOrderPlacedInHedgingMode()) {
            System.out.println("Hedging order(s) found, moving to fill form...");
            return;
        }

        System.out.println("Initiating market order...");

        Thread.sleep(1000);

        // select hedging mode
//        WebElement changeOrderMode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='NETTING']")));
        WebElement positionMode = driver.findElement(By.xpath("(//div[@class='css-1ktp5rg']/descendant::button[contains(@class, 'chakra-button')])[1]"));
        System.out.println("Current selected mode: " + positionMode.getText());

        if (positionMode.getText().equals("NETTING")) {
            positionMode.click();
            WebElement selectHedgingMode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[normalize-space()='Hedging Mode']")));
            selectHedgingMode.click();
            Thread.sleep(1000);
            WebElement clickConfirm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Confirm']")));
            clickConfirm.click();

            System.out.println("Mode changed to: " + selectHedgingMode.getText());
        }

        WebElement goToPositionsTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Positions']")));
        goToPositionsTab.click();

        WebElement selectMarketOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Market']")));
        selectMarketOrder.click();


        // place "Buy / Long" order
        int totalOrderCount = 1;
        int placeOrder = 0;
        System.out.println(MessageFormat.format("Starting placing a total of {0} orders", totalOrderCount));
        while (placeOrder < totalOrderCount) {
            driver.findElement(By.xpath("//button[@type='button']//p[contains(text(), 'Buy / Long')]")).click();
            WebElement confirmBuyOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Confirm']")));
            confirmBuyOrder.click();

            placeOrder += 1;

            Thread.sleep(2000);
        }

        System.out.println("A total of " + placeOrder + " market orders were placed.");

        // close the position
        // click "Close" button
        driver.findElement(By.xpath("//p[@class='chakra-text css-4jf6ui']")).click();
        System.out.println("Placed order was closed");
//
        // click "Close" button in confirmation popup
        WebElement closeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='Close']")));
        closeButton.click();
    }

}

//button[contains(@class, 'chakra-button')]//span[1]