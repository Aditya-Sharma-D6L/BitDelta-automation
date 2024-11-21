package prac;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GeneralSurvey {

    public WebDriver driver;
    private WebDriverWait wait;

    public GeneralSurvey(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void fillSurvey() {
        // Individual Questionnaire
        // Question 1 (Are you at least 18 years old) - click yes
        WebElement question1button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div/div/div[contains(@id, 'general_survey_1')]/label/span[contains(text(), 'Yes')]")));
        question1button.click();

        System.out.println("Filling General Survey...");

        // Question 2 (PEP) - click no
        WebElement question2button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div/div/div[contains(@id, 'general_survey_2')]/label/span[contains(text(), 'No')]")));
        question2button.click();

        // Question 3 (Do you have TIN) - click no
        WebElement question3button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div/div/div[contains(@id, 'general_survey_4')]/label[2]/span[contains(text(), 'No')]")));
        question3button.click();

        // Question 4 (Tax resident) - click no
        WebElement question4button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div/div/div[contains(@id, 'general_survey_6')]/label[2]/span[contains(text(), 'No')]")));
        question4button.click();

        // Question 5 (Invested in Crypto) - click no
        WebElement question5button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='general_survey_8']//span[@class='chakra-radio__label css-14iyyou'][normalize-space()='No']")));
        question5button.click();

        // Question 6 (Investment frequency) - click "1-10"
        WebElement question6button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='1-10']")));
        question6button.click();

        // Question 7 (Investment education) - click "I have attended..."
        WebElement question7button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='I have attended trading courses']")));
        question7button.click();

        // Question 8 (Which of the following statements are true:) - click "1st option"
        WebElement question8button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[contains(text(),'Cryptocurrency is a digital currency in which tran')]")));
        question8button.click();
    }

    public void fillAdditionalQuestions() {
        // Click "Next" button
        driver.findElement(By.xpath("//button[normalize-space()='Next']")).click();

        // Wait for next page to load
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Additional survey questions
        WebElement question9button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='Additional income']")));
        question9button.click();

        WebElement question10button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='$20k to $50k']")));
        question10button.click();

        WebElement question11button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='Potential gain/Potential loss: 40%/-24%']")));
        question11button.click();

        WebElement question12button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='general_survey_15']//span[@class='chakra-radio__label css-14iyyou'][normalize-space()='No']")));
        question12button.click();

        WebElement question13button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[id='general_survey_17'] label:nth-child(2) span:nth-child(2)")));
        question13button.click();

        WebElement question14button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[id='general_survey_19'] label:nth-child(2) span:nth-child(2)")));
        question14button.click();

        // Click "Next" button
        driver.findElement(By.xpath("//button[normalize-space()='Next']")).click();

        // Wait for the next page to load
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Client agreement
        WebElement clientAgreement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[1]//span[1]")));
        clientAgreement.click();

        // Risk Closure statement
        WebElement riskClosure = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[2]//span[1]")));
        riskClosure.click();

        // Disclaimers
        WebElement disclaimers = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[3]//span[1]")));
        disclaimers.click();

        // Click "Submit" button
        driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();

        System.out.println("General survey submitted");
    }

}
