package prac.review;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;
import java.util.Random;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WriteReview {

    public static void main(String[] args) throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get("http://qaclickacademy.com/practice.php");
        driver.manage().window().maximize();

        // wait for the page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[normalize-space()='Practice Page']")));

        // check any of the 3 options randomly
        int n = randomNumberGenerator();
        WebElement option = driver.findElement(By.xpath("(//label/input[@type='checkbox'])[" + n + "]"));
        option.click();

        // fetch the selected option text
        String optionText = driver.findElement(By.xpath("(//label/input[@type='checkbox'])[" + n + "]/parent::label")).getText();
        System.out.println(optionText);

        // select the option from dropdown
        driver.findElement(By.cssSelector("select[id='dropdown-class-example']")).sendKeys(optionText);

        // enter option text in the "Enter Your Name" input field
        driver.findElement(By.cssSelector("input[id='name']")).sendKeys(optionText);

        // click the alert button
        driver.findElement(By.cssSelector("input[id='alertbtn']")).click();

        // validate text in the alert popup
        Alert alert = driver.switchTo().alert();

        String actualString = alert.getText();

        if (actualString.contains(optionText)) {
            System.out.println("Alert contains the selected option name");
        } else {
            System.out.println("Alert doesn't contains the selected option name");
        }

        alert.accept();


    }

    public static int randomNumberGenerator() {
            Random rand = new Random();
        // Generates a random number between 0 and 2, then adds 1
        return rand.nextInt(3) + 1;
    }

}
