package prac.Derivatives;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import prac.copyTrading.DerivativesQuiz;
import prac.copyTrading.TransferFromSpotToDerivatives;

import java.time.Duration;

public class PlaceDerivativesOrder {

    public WebDriver driver;
    public WebDriverWait wait;
    public TransferFromSpotToDerivatives transfer;

    public PlaceDerivativesOrder(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(7));
        this.transfer = new TransferFromSpotToDerivatives(driver);
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
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='tabs-:r11:--tabpanel-3']/div//div[text()='HEDGING']")));

        } catch (NoSuchElementException | TimeoutException e) {
            System.out.println("No Hedging order(s) found");
            flag = false;
        } catch (Exception e) {
            System.out.println("Error occurred while looking for the 'HEDGING' element: " + e.getMessage());
        }

        return flag;
    }

    // This method check if user has sufficient available balance in derivatives
    private void checkDerivativesFunds (String env) throws InterruptedException {

        try {
            // look for the "Add Funds" popup
            WebElement addFundsPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Oops! It seems like there are not enough funds in your account. You can easily transfer funds from your Spot wallet to Trade on Derivatives']")));
            if (addFundsPopup.isDisplayed()) {
                System.out.println("No funds in derivatives, initiating transfer...");
                driver.get("https://" + env + ".bitdelta.com/en/wallet/overview");

                transfer.transferFromSpotToDerivatives();

                Thread.sleep(2000);
                // go back to the derivatives page
                driver.get("https://" + env + ".bitdelta.com/en/trade/derivatives/btc-usd");
            }
        } catch (NoSuchElementException | TimeoutException e) {
            // do nothing, proceed...
        }

        try {

            // check if the system shows insufficient balance
            // check if the balance is sufficient or not by comparing available balance with margin
            System.out.println();
            System.out.println("Checking if balance is sufficient...");

            // get margin value
            Thread.sleep(1000);
            WebElement marginText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='chakra-text css-11b8003']")));
            String margin = marginText.getText().replaceAll("[^0-9.]", "");
            double marginValue = Double.parseDouble(margin);

            // get available balance
            WebElement amountText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='chakra-text css-nwufdl']")));
            String amount = amountText.getText().replaceAll("[^0-9.]", "");
            double amountValue = Double.parseDouble(amount);

            // compare available balance with margin
            if (amountValue < (marginValue + 5)) {
                System.out.println("When available balance will be less than (margin amount + 5), then insufficient balance will be shown and transfer will be initiated");
                System.out.println();
                System.out.println("Available balance: " + amountValue);
                System.out.println("Insufficient balance, initiating transfer...");
                System.out.println();
                transfer.transferFromSpotToDerivatives();
                goToDerivatives();
            } else {
                System.out.println("Sufficient balance available: " + amountValue);
            }

            Thread.sleep(1000);
            System.out.println();

        } catch (NoSuchElementException | TimeoutException e) {
            // do nothing, proceed...
        }
    }

    public void placeMarketOrder(int derivativesOrderCount, String orderType, String env, boolean status) throws InterruptedException {

        // check if user has already placed a market order in Hedging mode
        if (status) {
            checkDerivativesFunds(env);

            // check if closed hedging orders are present
            if (checkIfAlreadyOrderPlacedInHedgingMode()) {
                System.out.println("Hedging order(s) found, moving to fill form...");
                return;
            }
        }

        // this if block will run only when not applying for master trader
        if (!status) {
            checkDerivativesFunds(env);
        }

        Thread.sleep(1000);

        // select hedging mode
//        WebElement changeOrderMode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='NETTING']")));
        WebElement positionMode = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class='css-1ktp5rg']/descendant::button[contains(@class, 'chakra-button')])[1]")));
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

        // select buy market order
        WebElement selectMarketOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Market']")));
        selectMarketOrder.click();

        // select order type
        if (orderType.equals("buy")) {
            // select buy order
            driver.findElement(By.xpath("//button[@type='button' and text()='Buy']")).click();
        } else if (orderType.equals("sell")) {
            driver.findElement(By.xpath("//button[@type='button' and text()='Sell']")).click();
        }

        if (status) {
            derivativesOrderCount = 1;
        }

        System.out.println();
        System.out.println("Initiating " + derivativesOrderCount + " derivatives " + orderType + " market order...");

        int count = 1;
        while (count <= derivativesOrderCount) {

            WebElement clickBuyLongButton;
            if (orderType.equals("buy")) { // when order type is buy
                clickBuyLongButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button']//p[contains(text(), 'Buy / Long')]")));
            } else { // when order type is sell
                clickBuyLongButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button']//p[contains(text(), 'Sell / Short')]")));
            }
            clickBuyLongButton.click();

            WebElement confirmBuyOrder = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Confirm']")));
            confirmBuyOrder.click();

            System.out.println(count + " order placed");
            count += 1;

            Thread.sleep(1000);
        }

        System.out.println("A total of " + derivativesOrderCount + " " + orderType + " market orders were placed.");

        // close the position
        if (status) {
            // click "Close" button
            driver.findElement(By.xpath("//p[@class='chakra-text css-4jf6ui']")).click();
            System.out.println("Placed order was closed");

            // click "Close" button in confirmation popup
            WebElement closeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='Close']")));
            closeButton.click();
        }
    }

}
