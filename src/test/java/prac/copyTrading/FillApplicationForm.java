package prac.copyTrading;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FillApplicationForm {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public FillApplicationForm(WebDriver driver) {
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
        Thread.sleep(8000);

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
    public class DisplayNameGenerator {

        // Set to store all previously generated names
        private static Set<String> generatedNames = new HashSet<>();

        public static String generateDisplayName() {
            return generateDisplayNames();
        }

        public static String generateDisplayNames() {
            // Arrays of meaningful parts for the name
            String[] adjectives = {"Swift", "Bold", "Silent", "Brave", "Noble", "Quick", "Loyal", "Mighty", "Clever", "Bright"};
            String[] animals = {"Tiger", "Eagle", "Wolf", "Lion", "Fox", "Shark", "Bear", "Falcon", "Panther", "Hawk"};
            String[] places = {"Mars", "Venus", "Oasis", "Atlantis", "Zenith", "Nebula", "Canyon", "Peak", "Summit", "Galaxy"};

            Random random = new Random();
            String newName;

            // Ensure that the name is unique
            do {
                // Select meaningful parts
                String adjective = adjectives[random.nextInt(adjectives.length)];
                String animal = animals[random.nextInt(animals.length)];
                String place = places[random.nextInt(places.length)];

                char digit = (char) ('0' + random.nextInt(10));  // Random digit
                char hyphen = '-';

                // Generate the base name structure (Adjective + Animal or Place + Digit)
                StringBuilder nameBuilder = new StringBuilder();
                nameBuilder.append(adjective).append(hyphen).append(animal).append(digit);

                // Adjust length to meet the constraints (10 to 16 characters)
                while (nameBuilder.length() < 10 + random.nextInt(7)) {
                    char randomLetter = (char) ('a' + random.nextInt(26));  // Random letter
                    nameBuilder.append(randomLetter);
                }

                newName = nameBuilder.toString();

            } while (generatedNames.contains(newName)); // Ensure the name is unique

            // Add the newly generated name to the set to keep track of it
            generatedNames.add(newName);

            // Return the unique name
            return newName;
        }

        public static void main(String[] args) {
            // Generate a few names to demonstrate uniqueness
            for (int i = 0; i < 10; i++) {
                System.out.println(generateDisplayName());
            }
        }
    }

}
