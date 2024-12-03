package prac;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KycAndSpot {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String env = "qa";

    private static final boolean approveKyc = true;
    private static final boolean approveSpotBalance = true;

    static String adminUrl = "https://" + env + "-admin.bitdelta.com/login";

    public KycAndSpot(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void initiateKycAndSpotBalance(String uid, String email, String spotBalanceAmount, boolean openInNewTab) throws InterruptedException {
        // Check openInNewTab and open a new tab if true
        if (openInNewTab) {
            driver.switchTo().newWindow(WindowType.TAB);
        }

        // Navigate to admin URL
        driver.get(adminUrl);

        // Login to the admin portal
        loginToAdmin();

        Thread.sleep(2000);
        // Click on "Users" tab on left panel
        driver.findElement(By.xpath("//div[contains(text(), 'Users ')]")).click();

        // Search by UID or Email based on provided input
        if (uid != null && !uid.isEmpty()) {
            searchByUID(uid);
        } else if (email != null && !email.isEmpty()) {
            searchByEmail(email);
        } else {
            throw new IllegalArgumentException("Either UID or Email must be provided.");
        }

        // Perform KYC approval
        if (approveKyc) {
            KYC kyc = new KYC(driver, wait);
            kyc.approveKYC(env);
        }

        // Update Spot Balance
        if (approveSpotBalance) {
            SpotBalance spotBalance = new SpotBalance(driver, wait);
            spotBalance.updateSpotBalance(spotBalanceAmount);
        }

        System.out.println("KYC and Spot Balance process completed.");
    }

    private void loginToAdmin() throws InterruptedException {

        String adminEmail = "";
        String adminPassword = "";

        if (env.equals("staging")) {
            adminEmail = "ashutosh.parihar@delta6labs.com";
            adminPassword = "Pass@12345";
        } else if (env.equals("qa")) {
            adminEmail = "laxman.kumar@bitdelta.com";
            adminPassword = "Pass@1234567";
        }

        // Enter admin email and password
        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(adminEmail);
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(adminPassword);
        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();

        // Handle 2FA if required
        handleTwoFa();
    }

    private void handleTwoFa() throws InterruptedException {
        Thread.sleep(2000);
        for (int i = 1; i <= 6; i++) {
            WebElement inputField = driver.findElement(By.xpath("(//input[@class='border rounded w-10 h-10 text-center form-control'])[" + i + "]"));
            inputField.sendKeys(String.valueOf(i)); // Example 2FA code for testing
        }

        driver.findElement(By.xpath("//button[contains(text(), 'Submit')]")).click();
    }

    private void searchByUID(String uid) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search by UID']"))).sendKeys(uid);
        driver.findElement(By.xpath("//button[normalize-space()='Go']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[@class='link']")).click(); // Open profile by ID
    }

    private void searchByEmail(String email) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search by email']"))).sendKeys(email);
        driver.findElement(By.xpath("//button[normalize-space()='Go']")).click();
        Thread.sleep(2000);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='" + email + "']/preceding::td[2]/a"))).click(); // Open profile by email
    }

    // Main method for independent execution
    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();

        // Define input values for manual execution
        String uid = "305689";             // Set UID if available, or leave blank if using email
        String email = ""; // Set email if UID is not used
        String spotBalanceAmount = "8000"; // Spot balance amount to be updated

        try {
            driver.get(adminUrl); // Open admin login page
            driver.manage().window().maximize();

            KycAndSpot kycAndSpot = new KycAndSpot(driver);

            // Call the method with provided parameters
            kycAndSpot.initiateKycAndSpotBalance(uid, email, spotBalanceAmount, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
