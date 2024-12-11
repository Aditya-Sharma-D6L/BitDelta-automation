package prac.copyTrading;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CopyTradingQuiz {

    public WebDriver driver;
    public WebDriverWait wait;

    public CopyTradingQuiz(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void performCopyTradingQuiz() throws InterruptedException {

        try {
            // look for "CT Quiz"
            WebElement performQuizNow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Perform Quiz Now']")));
            try {
                if (performQuizNow.isDisplayed()) {
                    System.out.println("Quiz not done, proceeding to complete it...");

                    // click "Perform Quiz Now"
                    performQuizNow.click();
                }
            } catch (NoSuchElementException | TimeoutException e) {
                System.out.println("Quiz already completed, proceeding to fill form...");
            }

            Thread.sleep(2000);

            WebElement nextButton = driver.findElement(By.xpath("//*[@type='button' and text()='Next']"));

            // Question 1 - "How much of my funds are at risk?"
            WebElement q1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='COPY_TRADING_QUESTION_1_ANSWER_2']")));
            q1.click();
            nextButton.click();

            // Question 2 - "Which product will the copy trader be using?"
            WebElement q2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='COPY_TRADING_QUESTION_2_ANSWER_2']")));
            q2.click();
            nextButton.click();

            // Question 3 - "How does the fixed amount copying work?"
            WebElement q3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='COPY_TRADING_QUESTION_5_ANSWER_3']")));
            q3.click();
            nextButton.click();

            // Question 4 - "What are the fees to be levied in copy trading?"
            WebElement q4 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='COPY_TRADING_QUESTION_6_ANSWER_3']")));
            q4.click();
            nextButton.click();

            // Question 5 - "What will happen in the case of insufficient margin while copying a trade?"
            WebElement q5 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='COPY_TRADING_QUESTION_7_ANSWER_2']")));
            q5.click();
            nextButton.click();

            // Question 6 - "How will copying multiple masters impact your current positions/balance allocated to copy trading?"
            WebElement q6 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='COPY_TRADING_QUESTION_8_ANSWER_1']")));
            q6.click();
            nextButton.click();

            // Question 7 - "Can you manually manage the copied position of the master?"
            WebElement q7 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='COPY_TRADING_QUESTION_3_ANSWER_3']")));
            q7.click();
            nextButton.click();

            // Question 8 - "How does the fixed percentage copying work?"
            WebElement q8 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='COPY_TRADING_QUESTION_4_ANSWER_1']")));
            q8.click();
            nextButton.click();

            // click "Submit" after quiz
            Thread.sleep(1500);
            WebElement startCopyTrading = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button' and text()='Start Copy Trading']")));
            startCopyTrading.click();
            Thread.sleep(1000);

        } catch (NoSuchElementException e) {
            System.out.println("No Quiz found: Looks like CT Quiz has been done.");
        } catch (TimeoutException e) {
//            System.out.println("Submit button was clicked, but visibility check failed");
            // do nothing
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Thread.sleep(1000);
    }
}
