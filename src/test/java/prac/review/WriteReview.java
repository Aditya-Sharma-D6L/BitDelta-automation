package prac.review;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class WriteReview {

    public static void main(String[] args) throws InterruptedException, IOException {

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get("https://rahulshettyacademy.com/AutomationPractice/");
        driver.manage().window().maximize();

        List<WebElement> links = driver.findElements(By.cssSelector("li[class=gf-li] a"));
        SoftAssert a = new SoftAssert();

        for (WebElement link: links) {
            String url = link.getAttribute("href");

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("HEAD");
            conn.connect();

            int statusCode = conn.getResponseCode();
            System.out.println(link.getText() + ": " + statusCode);

            a.assertTrue(statusCode < 400, "The link failed is: " + link.getText());
        }

        a.assertAll();


    }

}
