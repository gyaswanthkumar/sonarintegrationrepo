package config;

import com.aventstack.extentreports.ExtentTest;
import com.beust.jcommander.Parameterized;
import com.google.common.io.Files;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.joda.time.LocalDate;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;
import utility.ExtentManager;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import static config.Constants.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActionKeywords {
    public WebDriver driver;
    protected static String StoreVariable;
    protected static SoftAssert softAssert = new SoftAssert();
    Integer Delay;
    Integer Intervals;

    public boolean openBrowser(String object, String data, int dataRow, ExtentTest extent) {
        try {
            switch (data) {
                case "Firefox": {
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                }
                case "Edge": {
                 //   WebDriverManager.edgedriver().setup();
                  //  driver = new EdgeDriver();
                  //  DesiredCapabilities capabilities = new DesiredCapabilities();
                   //  capabilities.setCapability("demo_capability", true);
                     EdgeOptions options = new EdgeOptions();
                     options.addArguments("--disable-notifications");
                     options.setExperimentalOption("useAutomationExtension", false);
                     options.addArguments("start-maximized");
                     options.addArguments("enable-automation");
                     options.addArguments("--no-sandbox");
                     options.addArguments("--disable-infobars");
                     options.addArguments("--disable-dev-shm-usage");
                     options.addArguments("--disable-browser-side-navigation");
                     options.addArguments("--disable-gpu");
                     options.setPageLoadStrategy(PageLoadStrategy.NONE);
                      WebDriverManager.edgedriver().setup();
                      driver = new EdgeDriver(options);
                     
                  //   capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                   //  options.merge(capabilities);
                   //   WebDriverManager.edgedriver().setup();
                     // driver = new EdgeDriver();
                    // EdgeOptions options = new EdgeOptions();
                   // options.addArguments("--remote-allow-origins=*");
                    //WebDriver driver = new ChromeDriver(options);
                   
                    break;
                }
                case "Chrome": {
                 // System.setProperty("webdriver.chrome.driver","C:\\Users\\YaswanthKumarGollapo\\IdeaProjects\\Minwestpocnewfile1\\src\\main\\resources\\chromedriver.exe");
                 //  DesiredCapabilities capabilities = new DesiredCapabilities();
                  //  capabilities.setCapability("demo_capability", true);
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--disable-notifications");
                    options.setExperimentalOption("useAutomationExtension", false);
                    options.addArguments("start-maximized");
                    options.addArguments("enable-automation");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-infobars");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-browser-side-navigation");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--remote-allow-origins=*");
                    options.setPageLoadStrategy(PageLoadStrategy.NONE);
                 //  capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                 //   options.merge(capabilities);
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(options);
                  //  driver= (new RemoteWebDriver (new URL ("http://localhost:4444"), capabilities));
                    //  System.setProperty("webdriver.chrome.driver",".\\src\\main\\resources\\chromedriver.exe");
//                    WebDriverManager.chromedriver().setup();
                  // driver = new ChromeDriver();
                   break;



                }
            }
            driver.manage().window().maximize();
        }
        catch (JavascriptException e){

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }

          return true;
    }

    public boolean enterText(String object, String data, int dataRow, ExtentTest extent) throws Exception {
        try {
            WebElement element = elementFetching(Delay, object);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            element.sendKeys(Constants.data.get(dataRow).get(data));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean click(String object, String data, int dataRow, ExtentTest extent) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = elementFetching(Delay, object);
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            if (element != null) {
                Boolean isClickableAndDisplayed = (Boolean) js.executeScript(
                        "var element = arguments[0];" +
                                "var elementRects = element.getClientRects();" +
                                "var isClickableAndDisplayed = elementRects.length > 0 && elementRects[0].width > 0 && elementRects[0].height > 0 && !!(element.offsetWidth || element.offsetHeight || element.getClientRects().length);" +
                                "return isClickableAndDisplayed;", element);

                if (isClickableAndDisplayed) {
                    js.executeScript("arguments[0].click();", element);
                }
            }
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException | JavascriptException e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public WebElement elementFetching(int Delay, String object) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = null;
        int time = Intervals;
        do {
            try {
                element = driver.findElement(By.xpath(OR.getProperty(object)));
                js.executeScript("arguments[0].style.border='2px solid red'", element);
                break;
            } catch (NoSuchElementException | StaleElementReferenceException | JavascriptException e) {
                time = time + Intervals;
            }
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + COUNT * Intervals + ");");
        } while (time * 1000 < Delay);
        System.out.println(time);
        return element;
    }


    public WebElement elementlistFetching(int Delay, String object, int dataRow) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement element = null;
        int time = Intervals;
        do {
            try {
                for (WebElement ele : driver.findElements(By.xpath(OR.getProperty(object)))) {
                    if (ele.getText().equals(data.get(dataRow).get(data))) element = ele;
                }
                break;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                time = time + Intervals;
            }
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + COUNT * Intervals + ");");
        } while (time * 1000 < Delay);
        System.out.println(time);
        return element;
    }


    public boolean selectFromList(String object, String data, int dataRow, ExtentTest extent) {
        try {
            elementlistFetching(Delay, object, dataRow).click();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean navigate(String object, String data, int dataRow, ExtentTest extent) {

        try {
            driver.get(OR.getProperty(object));
            String[] time = data.split(",");
            Delay = Integer.valueOf(time[0]) * COUNT;
            Intervals = Integer.valueOf(time[1]);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean scrollToElement(String object, String data, int data_Row, ExtentTest extent) {

        try {
            WebElement ele = elementFetching(Delay, object);
            JavascriptExecutor scroll = (JavascriptExecutor) driver;
            scroll.executeScript("arguments[0].scrollIntoView(true);", ele);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean clickText(String object, String data, int dataRow, ExtentTest extent) {
        try {
            int time = Intervals;
            JavascriptExecutor js = (JavascriptExecutor) driver;
            do {
                try {
                    WebElement element = driver.findElement(By.xpath("//*[text()='" + Constants.data.get(dataRow).get(data) + "']"));
                    js.executeScript("arguments[0].style.border='2px solid red'", element);
                    Actions actions = new Actions(driver);
                    actions.click(element).perform();
                    break;
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    time = time + Intervals;
                }
                js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + COUNT * Intervals + ");");
            } while (time * 1000 < Delay);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean leftClick(String object, String data, int dataRow, ExtentTest extent) {

        try {
            WebElement element = elementFetching(Delay, object);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");

            Actions actions = new Actions(driver);
            actions.click(element).perform();

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean dropDownForValue(String object, String data, int dataRow, ExtentTest extent) {

        try {
            WebElement element = elementFetching(Delay, object);
            Select Element = new Select(element);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            Element.selectByValue(Constants.data.get(dataRow).get(data));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean dropDownForText(String object, String data, int dataRow, ExtentTest extent) {

        try {
            Select Element = new Select(elementFetching(Delay, object));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            Element.selectByVisibleText(data);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean getText(String object, String data, int dataRow, ExtentTest extent) {

        try {
            WebElement element = elementFetching(Delay, object);
            System.out.println(element.getText());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean getTextAndStore(String object, String data, int dataRow, ExtentTest extent) {
        try {
            WebElement element = elementFetching(Delay, object);
            StoreVariable = element.getText();
            System.out.println(StoreVariable);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean VerifyCalendar(String object, String data, int data_Row, ExtentTest extent) throws Exception {
        try {
            String s[] = Constants.data.get(data_Row).get(data).split(" ");
            while (!StoreVariable.equals(s[1])) {
                driver.findElement(By.xpath(OR.getProperty(object))).click();
            }
            driver.findElement(By.xpath("//label[text()='" + s[1] + "']")).click();
            return true;
        } catch (Exception e) {
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean verify(String object, String data, int dataRow, ExtentTest extent) {
        try {
            WebElement element = elementFetching(Delay, object);
            softAssert.assertEquals(element.getText(), StoreVariable);
            return true;
        } catch (AssertionError | Exception g) {
            System.out.println(g.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }


    public boolean scrollBy500(String object, String data, int data_Row, ExtentTest extent) {
        try {
            Dimension dme = driver.manage().window().getSize();
            int hgt = dme.getHeight();
            int wdt = dme.getWidth();
            int y = (int) (hgt * 0.2);
            int x = (int) (wdt * 0.2);
            int startx = (int) (wdt * 0.9);
            int endx = (int) (wdt * 0.1);
            int starty = (int) (hgt * 0.5);
            int endy = (int) (hgt * 0.35);
            int i = 0;
            while (i < 6) {
                JavascriptExecutor scroll = (JavascriptExecutor) driver;
                scroll.executeScript("window.scrollBy(0,-1000)", "");
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
        return true;
    }


    public boolean scrollBy1000(String object, String data, int data_Row, ExtentTest extent) {

        try {
            Dimension dme = driver.manage().window().getSize();
            int hgt = dme.getHeight();
            int wdt = dme.getWidth();
            int y = (int) (hgt * 0.2);
            int x = (int) (wdt * 0.2);
            int startx = (int) (wdt * 0.9);
            int endx = (int) (wdt * 0.1);
            int starty = (int) (hgt * 0.5);
            int endy = (int) (hgt * 0.35);
            int i = 0;
            while (i < 6) {
                JavascriptExecutor scroll = (JavascriptExecutor) driver;
                scroll.executeScript("window.scrollBy(0,1000)", "");
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
        return true;
    }

    public boolean getTitle(String object, String data, int dataRow, ExtentTest extent) {
        try {
            String Title = driver.getTitle();
            System.out.println(Title);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean verifyFromReferenceVariable(String object, String data, int dataRow, ExtentTest extent) {

        try {
            String Text = driver.findElement(By.xpath(OR.getProperty(object))).getText();
            assertThat(Text, (containsString(StoreVariable)));
            return true;
        } catch (AssertionError | Exception g) {
            System.out.println(g.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean verifyWithReferenceVariable(String object, String data, int dataRow, ExtentTest extent) {

        try {
            String Text = driver.findElement(By.xpath(OR.getProperty(object))).getText();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            assertThat(StoreVariable, (containsString(Text)));
            return true;
        } catch (AssertionError | Exception g) {
            System.out.println(g.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean setCalendar(String object, String data, int data_Row, ExtentTest extent) throws Exception {
        try {
            HashMap map = new HashMap();
            map.put("01", "January");
            map.put("02", "February");
            map.put("03", "March");
            map.put("04", "April");
            map.put("05", "May");
            map.put("06", "June");
            map.put("07", "July");
            map.put("08", "August");
            map.put("09", "September");
            map.put("10", "October");
            map.put("11", "November");
            map.put("12", "December");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = elementFetching(Delay, object);
            element.click();
            LocalDate d = LocalDate.now();
            String[] date = d.toString().split("-");
            String currentDate = date[2];
            String currentMonth = map.get(date[1]).toString();
            String currentYear = date[0];
            String[] rDate = Constants.data.get(data_Row).get(data).split("/");
            String reqDate = rDate[1];
            String reqMonth = map.get(rDate[0]).toString();
            String reqYear = rDate[2];

            driver.findElement(By.xpath("//*[contains(text(),'" + currentMonth + "')]")).click();
            driver.findElement(By.xpath("//*[@year='" + reqYear + "']")).click();
            driver.findElement(By.xpath("//*[text()='" + reqMonth.substring(0, 3) + "']")).click();
            driver.findElement(By.xpath("//*[contains(text(),'" + reqMonth + "')]/following-sibling::div[1]")).click();

            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");

            if (reqDate.substring(0, 1).equals("0"))
                driver.findElement(By.xpath("//*[text()='" + reqDate.substring(1) + "']")).click();
            else
                driver.findElement(By.xpath("//*[text()='" + reqDate + "']")).click();

            return true;

        } catch (Exception e) {
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean verifySubstring(String object, String data, int dataRow, ExtentTest extent) {

        try {
            String Text = driver.findElement(By.xpath(OR.getProperty(object))).getText();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            assertThat(Text, (containsString(Constants.data.get(dataRow).get(data))));
            return true;
        } catch (AssertionError | Exception g) {
            System.out.println(g.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean getCurrentUrl(String object, String data, int dataRow, ExtentTest extent) {

        try {
            String Url = driver.getCurrentUrl();
            System.out.println(Url);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean doubleClick(String object, String data, int dataRow, ExtentTest extent) {

        try {
            WebElement element = elementFetching(Delay, object);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            Actions actions = new Actions(driver);
            actions.doubleClick(element).perform();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }

    }

    public boolean clear(String object, String data, int dataRow, ExtentTest extent) {

        try {
            WebElement element = elementFetching(Delay, object);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            element.clear();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean fileUpload(String object, String data, int dataRow, ExtentTest extent) throws AWTException {

        try {
            Robot rb = new Robot();

            rb.setAutoDelay(1000);

            StringSelection ss = new StringSelection((Constants.data.get(dataRow).get(data)));
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

            rb.keyPress(KeyEvent.VK_CONTROL);
            rb.keyPress(KeyEvent.VK_V);

            //key release the ctrl and v from keyboard

            rb.keyRelease(KeyEvent.VK_CONTROL);
            rb.keyRelease(KeyEvent.VK_V);

            //key press and release the enter from keyboard

            rb.keyPress(KeyEvent.VK_ENTER);
            rb.keyRelease(KeyEvent.VK_ENTER);
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean EnterNumber(String object, String data, int dataRow, ExtentTest extent) throws AWTException {

        try {

            Robot rb = new Robot();
            String[] Number = Constants.data.get(dataRow).get(data).split("");
            for (String number : Number) {
                driver.findElement(By.xpath(OR.getProperty(object))).click();
                StringSelection ss = new StringSelection(number);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

                rb.keyPress(KeyEvent.VK_CONTROL);
                rb.keyPress(KeyEvent.VK_V);

                //key release the ctrl and v from keyboard

                rb.keyRelease(KeyEvent.VK_CONTROL);
                rb.keyRelease(KeyEvent.VK_V);

                //key press and release the enter from keyboard

                rb.keyPress(KeyEvent.VK_TAB);
                rb.keyRelease(KeyEvent.VK_TAB);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
        return true;
    }

    public boolean closeBrowser(String object, String data, int dataRow, ExtentTest extent) {
        try {
            System.out.println("Closed the Browser");
            softAssert.assertAll();
            driver.quit();
            return true;
        } catch (AssertionError | Exception g) {
            System.out.println(g.getMessage());
            System.out.println("Failed");
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }


    public boolean getAttribute(String object, String data, int dataRow, ExtentTest extent) {

        try {
            WebElement ele = driver.findElement(By.xpath(OR.getProperty(object)));
            System.out.println(ele.getAttribute(data));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean getAttributeAndStore(String object, String data, int dataRow, ExtentTest extent) {

        try {
            WebElement ele = driver.findElement(By.xpath(OR.getProperty(object)));
            StoreVariable = ele.getAttribute(data);
            System.out.println(StoreVariable);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }


    public boolean alert(String object, String data, int dataRow, ExtentTest extent) {

        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println(alertText);
            handleAlert(alert, data);
        } catch (AssertionError | Exception g) {
            System.out.println(g.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
        return true;
    }

    public boolean fullPageScreenshot(String object, String data, int dataRow, ExtentTest extent) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            Date d = new Date();
            String screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
            String path = ".\\ScreenShots\\" + screenshotName;
            File desc = new File(path);
            Files.copy(src, desc);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }

    public boolean elemenetScreenshot(String object, String data, int dataRow, ExtentTest extent) {

        try {
            WebElement element = elementFetching(Delay, object);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeAsyncScript("setTimeout(arguments[arguments.length - 1]," + Intervals * COUNT + ");");
            File src = element.getScreenshotAs(OutputType.FILE);
            Date d = new Date();
            String screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
            String path = ".\\ScreenShots\\" + screenshotName;
            File desc = new File(path);
            Files.copy(src, desc);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ExtentManager.takescreenshot(driver, extent);
            return false;
        }
    }


    public static void handleAlert(Alert alert, String action) {
        switch (action.toLowerCase()) {
// The switch statement checks the value of the action parameter after converting it to lowercase.
            case "accept":
            case "ok":
            case "yes":
                alert.accept();
                break;
            case "dismiss":
            case "cancel":
            case "no":
                alert.dismiss();
                break;
            default:
                alert.sendKeys(action);
                break;
        }
    }
}


