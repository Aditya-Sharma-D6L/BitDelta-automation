package prac.copyTrading;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ApplyMasterTrader {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ApplyMasterTrader(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void goToCopyTrading () throws InterruptedException {

        // click on "Copy Trading" in header
        WebElement clickCopyTrading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'Copy Trading')]")));
        clickCopyTrading.click();
        Thread.sleep(2000);

        // click "Apply to be a Master Trader"
        WebElement clickApplyToBeMaster = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Apply to be a Master Trader']")));
        clickApplyToBeMaster.click();

        // check if user has already applied
        if (checkIfAlreadyApplied()) {
            return;
        }

        // perform "Copy Trading" quiz
        try {
            Thread.sleep(2000);
            System.out.println("Looking for Copy Trading Quiz...");
            CopyTradingQuiz copyTradingQuiz = new CopyTradingQuiz(driver);
            copyTradingQuiz.performCopyTradingQuiz();

            try {
                // click "Start Copy Trading" button
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='chakra-modal-:ra:']")));
                WebElement clickStart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='chakra-modal--body-:ra:']/div/div/button")));

                // Click the button
                clickStart.click();
                System.out.println("Quiz completed and submitted");
            } catch (NoSuchElementException | TimeoutException e) {
                System.out.println("Quiz already done");
                System.out.println("Proceeding to fill the form...");
            } catch (Exception e) {
                System.out.println("An unexpected exception occurred: " + e.getMessage());
            }


        } catch (Exception e) {
            System.out.println("Error filling Copy Trading Quiz or the Quiz has been already done: " + e.getMessage());
            e.printStackTrace();
            e.getLocalizedMessage();
        }

        // start filling application
        Thread.sleep(1000);
        FillApplication();

        Thread.sleep(1500);
        handleTnCPopup();

        goToDashboard();

        System.out.println("Master application submitted successfully.");
    }

    private boolean checkIfAlreadyApplied() {

        boolean flag = false;

        try {
            WebElement checkPatienceText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/p[contains(text(), 'Please have some patience.')]")));
            if (checkPatienceText.isDisplayed()) {
                System.out.println("This user has already applied for master trader");
                flag = true;
            }
        } catch (TimeoutException e) {
            System.out.println("User has not applied for master");
        }

        return flag;
    }

    private void goToDashboard () throws InterruptedException {
        Thread.sleep(1000);
        WebElement goToDashBoard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Go to Dashboard']")));
        goToDashBoard.click();
    }

    private void handleTnCPopup() {
        try {

//            WebElement scrollButton = driver.findElement(By.xpath("//div[contains(text(),'Scroll Down')]"));
            WebElement scrollButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Scroll Down')]")));

            // Ensure the element is scrolled into view before clicking
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", scrollButton);

            scrollButton.click();
            Thread.sleep(1000);

            // click checkbox to agree TnC
//            driver.findElement(By.xpath("//input[@type='checkbox']")).click();
            WebElement checkBox = driver.findElement(By.xpath("//label/span/p[text()='I agree to the BitDelta Terms and conditions']"));
            checkBox.click();

            WebElement acceptButton = driver.findElement(By.xpath("//button[normalize-space()='Agree']"));
            acceptButton.click();
        } catch (NoSuchElementException e) {
            System.out.println("TnC already accepted");
        }
        catch (Exception e) {
            System.out.println("TnC popup not found or could not be handled.");
        }
    }

    public void FillApplication() throws InterruptedException {
        FillApplicationForm form = new FillApplicationForm(driver);
        form.fillApplication();
    }

}
