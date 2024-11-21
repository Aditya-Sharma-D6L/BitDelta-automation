package prac.copyTrading;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DerivativesQuiz {
    public WebDriver driver;
    public WebDriverWait wait;

    public DerivativesQuiz(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    private static void clickNextButton(WebDriver driver) throws InterruptedException {
        // click "Next" button
        driver.findElement(By.xpath("//button[contains(text(), 'Next')]")).click();
        Thread.sleep(1000);
    }

    public void performDerivativesQuiz(WebDriver driver) throws InterruptedException {

        // wait for the page to load
        Thread.sleep(2000);

        try {

            //click on "perform" button
            driver.findElement(By.xpath("//button[contains(text(), 'Perform Quiz Now')]")).click();

            System.out.println("Performing derivatives quiz...");

            // Perform quiz
            // Question 1 - What will you do when you fail to place order due to size limit of the order?
            WebElement question1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='DERIVATIVE_QUESTION_1_ANSWER_2']")));
            question1.click();
            clickNextButton(driver);

            //Question 2 - What is the difference between Limit order and Market order?
            WebElement question2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='DERIVATIVE_QUESTION_2_ANSWER_1']")));
            question2.click();
            clickNextButton(driver);

            // Question 3 - Which is the following description of PnL is correct?
            WebElement question3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='DERIVATIVE_QUESTION_3_ANSWER_3']")));
            question3.click();
            clickNextButton(driver);


            // Question 4 - How much loss user can have during Derivative trading?
            WebElement question4 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='DERIVATIVE_QUESTION_5_ANSWER_1']")));
            question4.click();
            clickNextButton(driver);


            // Question 5 - When will the all the position be Liquidated automatically?
            WebElement question5 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='DERIVATIVE_QUESTION_6_ANSWER_1']")));
            question5.click();
            clickNextButton(driver);

            // Question 6 - Derivative fees includes:
            WebElement question6 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='DERIVATIVE_QUESTION_7_ANSWER_2']")));
            question6.click();
            clickNextButton(driver);

            // Question 7 - While trading on the derivative, which type of attitude can help the user?
            WebElement question7 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='DERIVATIVE_QUESTION_9_ANSWER_1']")));
            question7.click();

            // click "Submit"
            driver.findElement(By.xpath("//button[@type='button' and text()='Submit']")).click();
            Thread.sleep(1000);

            // click "Start trading" button in congratulations popup
            WebElement startTradingButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Start Trading']")));
            startTradingButton.click();

            System.out.println("Derivatives quiz completed");

        } catch (NoSuchElementException | TimeoutException e) {
            // continue
        }

        Thread.sleep(1000);
    }
}
