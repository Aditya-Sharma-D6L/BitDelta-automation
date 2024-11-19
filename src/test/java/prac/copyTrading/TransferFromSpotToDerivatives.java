package prac.copyTrading;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TransferFromSpotToDerivatives {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public TransferFromSpotToDerivatives(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    private boolean checkDerivativesBalance() throws InterruptedException {

        boolean flag = false;

        // go to "Derivatives Wallet"
        // logic to checked if derivatives wallet has some amount
        // go to Derivatives wallet
        WebElement derivativesWallet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/en/wallet/derivatives']//p[text()='Derivatives']")));
        derivativesWallet.click();

        // hide eye icon click
        driver.findElement(By.xpath("//p[@class='chakra-text css-4g6ai3']//*[name()='svg']//*[name()='path' and contains(@stroke-linecap,'round')]")).click();
        Thread.sleep(1000);

        // check for amount in DW
        WebElement availableDerivativesBalance = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='chakra-text css-13c4fus']")));
        String balanceText = availableDerivativesBalance.getText();

        balanceText = balanceText.replaceAll(",", ""); // Remove commas
        balanceText = balanceText.replaceAll("[^0-9.]]", ""); // Remove any non-numeric characters except the decimal point

        Thread.sleep(1500);
        double balance = Double.parseDouble(balanceText);

        if (balance >= 100) {
            System.out.println("Available Derivatives balance: " + balance);
            flag = true;
        }
        return flag;
    }

    public void transferFromSpotToDerivatives() throws InterruptedException {

        // go to the wallet
        WebElement clickWallet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/p[contains(text(), 'Wallet')]")));
        clickWallet.click();

        if (checkDerivativesBalance()) {
            return;
        } else {

            System.out.println("No balance in derivatives wallet, initiating transfer from Spot");

            // click spot
            WebElement spotAccount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/en/wallet/spot']//p[text()='Spot']")));
            spotAccount.click();

            // click transfer button
            Thread.sleep(3000);
            WebElement transferButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Transfer']")));
            transferButton.click();

            // enter amount to transfer
            WebElement transferableAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='chakra-text css-13c4fus']")));
            String amount = transferableAmount.getText();
            WebElement inputAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter amount']")));
            inputAmount.sendKeys(amount);

            // click "Transfer" button
            Thread.sleep(1000);
            driver.findElement(By.xpath("//button[@class='chakra-button css-11d53u5' and text()='Transfer']")).click();

            // confirm warning message
            WebElement warningConfirm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Confirm']")));
            warningConfirm.click();

            // click "Close" after Transfer Successful
            WebElement close = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Close']")));
            close.click();
        }
    }
}
