package test;

import config.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static config.Constants.OR;

public class testing2 {
    static WebDriver driver;
    static int i = 0;
    static int Delay;
    static testing2 t;

    public static void main(String[] args) throws AWTException, InterruptedException {
        for (int i = 0; i <= 5; i++) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
            driver.manage().window().maximize();
            driver.get("https://e2.minnwestbank.com/apps/onlinebanking/#_frmLogin");
            Delay = 30000;
            t = new testing2();
            t.UserName();
            t.Password();
//        System.out.println(System.currentTimeMillis());
//        UserName1("//input[@placeholder=\"Password\"]", "Rayudu@123#");
//        Thread.sleep(10000);
            t.click();
            driver.quit();
        }
    }

    JavascriptExecutor js = (JavascriptExecutor) driver;
    static String script = "var element = arguments[0];" +
            "if (element.offsetParent !== null) {" +
            "var elementRects = element.getClientRects()[0];" +
            "if (elementRects.width > 0 || elementRects.height > 0) {" +
            "element.click();" +
            "}" +
            "}";


    public WebElement Delay(int Delay, String object) {
        WebElement element = null;
        int time = 0;
        do {
            try {
                js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + 1000 * time + ");");
                element = (WebElement) js.executeScript(
                        "var xpath = arguments[0];" +
                                "var result = document.evaluate(xpath, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null);" +
                                "var element = result.singleNodeValue;" +
                                "return element;", object
                );

                if (element != null) break;
                time = time + 5;
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        } while (time * 1000 < Delay);
        System.out.println(time);
        return element;
    }

    public void UserName() {
        String xpath = "//input[@placeholder='Username']";
        WebElement element = Delay(Delay, xpath);
        element.sendKeys("Akki");
    }

    public void Password() {
        String xpath = "//input[@placeholder=\"Password\"]";
        WebElement element = Delay(Delay, xpath);
        element.sendKeys("akhil");

    }

    public void click() {
        try {
            String xpath = "//label[@title=\"Log In\"]";
            WebElement element = Delay(Delay, xpath);
            Boolean isClickableAndDisplayed = (Boolean) js.executeScript(
                    "var element = arguments[0];" +
                            "var elementRects = element.getClientRects()[0];" +
                            "var isClickableAndDisplayed = elementRects.width > 0 && elementRects.height > 0 && !!(element.offsetWidth || element.offsetHeight || element.getClientRects().length);" +
                            "return isClickableAndDisplayed;", element);
            if (isClickableAndDisplayed) {
                js.executeScript("arguments[0].click();", element);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void waitForElementToBeClickableAndDisplayed(String cssSelector, long timeoutInMillis) {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        WebElement element = null;
        do {
            try {
                element = (WebElement) ((JavascriptExecutor) driver).executeScript("return document.querySelector('" + cssSelector + "');");
                if (element != null && element.isEnabled() && element.isDisplayed()) {
                    element.click();
                    return;
                }
            } catch (NoSuchElementException | StaleElementReferenceException ignored) {
            }

            elapsedTime = System.currentTimeMillis() - startTime;
        } while (elapsedTime < timeoutInMillis);
        throw new TimeoutException("Element " + cssSelector + " not found or not clickable within " + timeoutInMillis + " milliseconds");
    }


}

