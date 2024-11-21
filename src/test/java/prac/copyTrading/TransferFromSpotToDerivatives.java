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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    public boolean checkDerivativesBalance() throws InterruptedException {

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
        double balance = checkBalanceInWallet();

        if (balance >= 100) {
            System.out.println("Available Derivatives balance: " + balance);
            flag = true;
        }
        return flag;
    }

    // this method checks balance in both spot and derivatives wallets depending on which wallet page it is
    private double checkBalanceInWallet () throws InterruptedException {
        // check for amount in DW
        Thread.sleep(1000);
        WebElement availableDerivativesBalance = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='chakra-text css-13c4fus']")));
        String balanceText = availableDerivativesBalance.getText();

        balanceText = balanceText.replaceAll(",", ""); // Remove commas
        balanceText = balanceText.replaceAll("[^0-9.]]", ""); // Remove any non-numeric characters except the decimal point

        Thread.sleep(1500);

        return Double.parseDouble(balanceText);
    }

    public void transferFromSpotToDerivatives() throws InterruptedException {

        // click wallet and go to overview
        WebElement clickWallet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/p[contains(text(), 'Wallet')]")));
        clickWallet.click();
        WebElement overview = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[normalize-space()='Overview']")));
        overview.click();

        if (checkDerivativesBalance()) {
            return;
        } else {

            System.out.println("Current derivatives balance: " + checkBalanceInWallet());
            System.out.println("Initiating transfer from Spot...");

            // click spot
            WebElement spotAccount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/en/wallet/spot']//p[text()='Spot']")));
            spotAccount.click();
            System.out.println("Current Spot balance: " + checkBalanceInWallet());

            // click transfer button
            Thread.sleep(3000);
            WebElement transferButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Transfer']")));
            transferButton.click();

            // enter amount to transfer
            WebElement transferableAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='chakra-text css-13c4fus']")));
            String amountText = transferableAmount.getText().replace(",", "");
            double amount = (Double.parseDouble(amountText)) / 2;
            amountText = String.valueOf(amount);

            System.out.println("Transferring half of spot amount to derivatives wallet: " + amountText);

            WebElement inputAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter amount']")));
            inputAmount.sendKeys(amountText);

            // click "Transfer" button
            Thread.sleep(1000);
            driver.findElement(By.xpath("//button[@class='chakra-button css-11d53u5' and text()='Transfer']")).click();

            // confirm warning message
            WebElement warningConfirm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Confirm']")));
            warningConfirm.click();

            // click "Close" after Transfer Successful
            WebElement close = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Close']")));
            close.click();

            Thread.sleep(1000);
            System.out.println("Amount transfer successful, \n amount transferred: " + amountText);

            driver.navigate().refresh();
            WebElement derivativesWallet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/en/wallet/derivatives']//p[text()='Derivatives']")));
            derivativesWallet.click();
            Thread.sleep(1000);
            System.out.println("Latest derivatives balance: " + checkBalanceInWallet());
        }
    }
}
