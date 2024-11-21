package prac.Derivatives;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PlaceSpotOrder {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PlaceSpotOrder(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void handleTnCPopup() {
        try {
            try {
//                WebElement scrollButton = driver.findElement(By.xpath("//div[contains(text(),'Scroll Down')]"));
                WebElement scrollButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Scroll Down')]")));

                // Ensure the element is scrolled into view before clicking
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", scrollButton);

                scrollButton.click();
            } catch (NoSuchElementException | TimeoutException e) {

                Thread.sleep(1000);

                WebElement checkBox = driver.findElement(By.xpath("//label/span/p[text()='I agree to the BitDelta Terms and conditions']"));
                checkBox.click();

                WebElement acceptButton = driver.findElement(By.xpath("//button[normalize-space()='Agree']"));
                acceptButton.click();
            }
        } catch (Exception e) {
            System.out.println("TnC not found, continuing...");
        }
    }

    public void goToSpotPage() throws InterruptedException {

        WebElement clickTradeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/p[text()='Trade']")));
        clickTradeButton.click();

        WebElement goToSpotPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='navigation-bar-item-menu undefined']//p[normalize-space()='Spot']")));
        goToSpotPage.click();
        System.out.println("Going to spot page...");
        Thread.sleep(2000);

        // handle Spot TnC
        handleTnCPopup();

        // select market order tab
        WebElement marketTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='button' and text()='Market']")));
        marketTab.click();
    }

    public void placeBuyMarketOrders(int spotOrderCount, String orderType, String amount) throws InterruptedException {

        int count = 1;

        System.out.println("Initiating " + spotOrderCount + "spot " + orderType + " market order(s):");
        while (count <= spotOrderCount) {

            Thread.sleep(1000);

            // click "Buy" if order type is "Buy"
            if (orderType.equals("buy")) {

                // enter amount in "total" input field for market order
                driver.findElement(By.xpath("//input[@placeholder='Total']")).sendKeys(amount);

                // click buy button
                driver.findElement(By.xpath("//button[@type='submit' and text()='Buy']")).click();
                System.out.println(count + " order placed");

            } else if (orderType.equals("sell")) {

                int sentAmount = Integer.parseInt(String.valueOf(amount));
                double sellAmount = (double) sentAmount/spotOrderCount;

                // enter amount in "total" input field for market order
                driver.findElement(By.xpath("//input[@placeholder='Amount']")).sendKeys(Double.toString(sellAmount));

                // click sell button
                driver.findElement(By.xpath("//button[@type='submit' and text()='Sell']")).click();
            } else {
                System.out.println("Error placing spot market " + orderType + " order");
            }

            count += 1;
        }
        System.out.println();
        System.out.println(spotOrderCount + " " + orderType + " market orders placed successfully");
    }
}
