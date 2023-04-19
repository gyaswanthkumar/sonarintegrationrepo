package utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;

import static config.Constants.*;
import static executionEngine.DriverScript.sParallelMode;
import static utility.ExcelUtils.getCellData;

public class ExtentManager {
    static String Path;

    public static JiraServiceProvider onTestFailure() {
        JiraServiceProvider jiraSp = new JiraServiceProvider("https://akhil1467.atlassian.net",
                "akhil.k@kairostech.com", "wECFw06q2iyKyARgLEy3A7AE", "KIT");
        return jiraSp;
    }

    public static String Path() {
        return Path;
    }

    public static ExtentReports Report(int suite_Loop) throws Exception {
        String FileMode = "";
        switch (sParallelMode) {
            case "Yes": {
                FileMode = "_Parallel.html";
                break;
            }
            case "No": {
                FileMode = ".html";
                break;
            }
        }
        Path = ".\\report\\" +
                ExcelUtils.getCellData(suite_Loop - 1, Col_TestSuite, Sheet_TestSuites) + FileMode;
        ExtentSparkReporter spark = new ExtentSparkReporter(Path);
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "K Akhil");
        spark.config().setReportName("KiTAP-KeyWord");
        return extent;
    }

    public static void takescreenshot(WebDriver driver, ExtentTest extent) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            Date d = new Date();
            String screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
            String path = ".\\ScreenShots\\" + screenshotName;
            File desc = new File(path);
            Files.copy(src, desc);
            AttachScreenshotToReport(path, extent);
        } catch (Exception e) {

        }
    }

    public static void Pass(ExtentTest extentReportStatus, String Description) {
        extentReportStatus.log(Status.PASS, Description);
    }

    public static JiraServiceProvider Fail(ExtentTest extentReportStatus, String Description) {
        extentReportStatus.log(Status.FAIL, Description);
        return onTestFailure();
    }

    public static void Skip(ExtentTest extentReportStatus, String Description) {
        extentReportStatus.log(Status.SKIP, Description);
    }


    public static void AttachScreenshotToReport(String Path, ExtentTest extent) {
        try {
            InputStream is = new FileInputStream(Path);
            byte[] pic = IOUtils.toByteArray(is);
            String sample = Base64.getEncoder().encodeToString(pic);
            extent.addScreenCaptureFromBase64String(sample);
        } catch (Exception e) {

        }
    }


    public static String Description(String actionKeywords, String sPageObject, int iTestStep,
                                     String sTestCaseID, int DataSet_Row, String sData) throws Exception {
        String sCellDat = "";
        String setDate = data.get(DataSet_Row).get(sData);
        if (!sPageObject.equals("Password")) {
            if (setDate != null)
                sCellDat = getCellData(iTestStep, Col_PageObject - 2, sTestCaseID) +
                        ": " + getCellData(iTestStep, Col_PageObject - 1, sTestCaseID) + ": " +
                     "<b>"+   data.get(DataSet_Row).get(sData)+"</b>";
            else
                sCellDat = getCellData(iTestStep, Col_PageObject - 2, sTestCaseID) +
                        ": " + getCellData(iTestStep, Col_PageObject - 1, sTestCaseID);

        } else {
            sCellDat = getCellData(iTestStep, Col_PageObject - 2, sTestCaseID) +
                    ": " + getCellData(iTestStep, Col_PageObject - 1, sTestCaseID);
        }
        return sCellDat;
    }

}
