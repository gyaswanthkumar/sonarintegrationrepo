package executionEngine;

import com.aventstack.extentreports.ExtentReports;
import config.ActionKeywords;
import config.Constants;
import utility.ExcelUtils;
import utility.ExtentManager;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.*;
import static config.Constants.*;
import static utility.ExcelUtils.*;

public class DriverScript {
    private Thread t1;
    public static boolean bResult;
    public static String sParallelMode;
    public DriverScript() {
        actionKeywords = new ActionKeywords();
        for (Method t : actionKeywords.getClass().getDeclaredMethods()) {
            WebMethods.put(t.getName(), t);
        }
    }

    public static void main(String[] args) {
        try {
            OR = new Properties(System.getProperties());
            OR.load(new FileInputStream(Constants.Path_OR));
            ExcelUtils.setExcelFile(ExcelPath);
            data = ExcelUtils.getData(ExcelPath, Sheet_DataSet);
            DriverScript start = new DriverScript();
            start.execute_Suite();
        } catch (Exception e) {
        }
    }

    private void execute_Suite() {
        try {
            int rows = getRowCount(Sheet_TestSuites);
            int MaxSteps = 0;
            for (int SuiteLoop = 1; SuiteLoop < rows; SuiteLoop++) {
                if (!getCellData(SuiteLoop, Col_TestSuite, Sheet_TestSuites).isEmpty()) {
                    sRunMode = getCellData(SuiteLoop, Col_RunMode, Sheet_TestSuites);
                    sParallelMode = getCellData(SuiteLoop, Col_ParallelMode, Sheet_TestSuites);
                    driver_Type = getCellData(SuiteLoop, Col_DriverType, Sheet_TestSuites);
                    continue;
                }
                if (sRunMode.equals("Yes")) {
                    ExtentReports extent = ExtentManager.Report(SuiteLoop);
                    for (int Data_Row1 : split(SuiteLoop - 1, 3)) {
                        DataSet_Row = Data_Row1;
                        int k = SuiteLoop;
                        TotalCases = getCasesCount(Sheet_TestSuites, k, 1);
                        for (; k < SuiteLoop + TotalCases; k++) {
                            sTestCaseID = getCellData(k, 1, Sheet_TestSuites);
                            iTestLastStep = getRowCount(sTestCaseID);
                            int DataSet = Integer.valueOf(DataSet_Row) + 1;
                            ExtentReportStatus = extent.createTest(getCellData(k,
                                    2, Sheet_TestSuites)
                                    + ": " + DataSet + " (" + getCellData(1, 4, sTestCaseID) + ")");
                            Thread t = new Thread(new KeyWordDriven(sTestCaseID, iTestLastStep,
                                    SuiteLoop, ExtentReportStatus, driver_Type, DataSet_Row, extent));
                            t.start();
                            if (sParallelMode.equals("Yes")) {
                                if (MaxSteps < iTestLastStep) {
                                    t1 = t;
                                    MaxSteps = iTestLastStep;
                                }
                            } else {
                                t.join();
                            }
                        }
                    }
                    SuiteLoop = SuiteLoop + TotalCases - 1;
                    if (sParallelMode.equals("Yes")) t1.join();
                }
//                if (sRunMode.equals("Yes"))
              //  Sendmail.mail(ExtentManager.Path());
            }
        } catch (Exception e) {
        }
    }
}