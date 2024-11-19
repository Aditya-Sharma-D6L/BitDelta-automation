package prac.copyTrading;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Random;

public class FillApplicationForm {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public FillApplicationForm (WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void fillApplication() throws InterruptedException {

        // fill a Display name
        WebElement displayName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter your name']")));
        displayName.sendKeys(DisplayNameGenerator.generateDisplayName());

        // fill "Name of one platform"
        WebElement platformName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter name of platform']")));
        platformName.sendKeys("BitDelta");

        // upload image
        WebElement uploadImage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/span[@class='label' and text()='Upload Documents']")));
        uploadImage.click();

        // manually select an image within this time frame
        Thread.sleep(7000);

        // fill experience
        WebElement tradingExperience = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@placeholder='Content']")));
        String description = """
                Registered on the BitDelta platform at least 30 days ago.
                Traded minimum amount of 1000 USDT and from the last 30 days he/she has to perform trade for at least 7 days.
                His/Her ROI should be an average minimum 50% in the last 15 days.
                Master trader can have 1000 copiers maximum.""";
        tradingExperience.sendKeys(description);
        Thread.sleep(1500);

        // submit the form
        WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit' and text()='Submit']")));
        submit.click();
    }

    // generate random Display name
    public static class DisplayNameGenerator {

        public static String generateDisplayName() {
            return generateDisplayNames();
        }

        public static String generateDisplayNames() {
            String letters = "abcdefghijklmnopqrstuvwxyz";
            String digits = "0123456789";
            Random random = new Random();

            // Ensure mandatory parts
            char capitalLetter = Character.toUpperCase(letters.charAt(random.nextInt(letters.length())));
            char digit = digits.charAt(random.nextInt(digits.length()));
            char hyphen = '-';

            // Generate remaining random characters
            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(capitalLetter).append(hyphen).append(digit);

            while (nameBuilder.length() < 10 + random.nextInt(7)) { // To keep length between 10-16 characters
                nameBuilder.append(letters.charAt(random.nextInt(letters.length())));
            }

            return nameBuilder.toString();
        }

        private static String shuffle(String input, Random random) {
            char[] array = input.toCharArray();
            for (int i = array.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                char temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
            return new String(array);
        }
    }
}
