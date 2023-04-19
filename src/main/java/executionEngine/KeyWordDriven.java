package executionEngine;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import config.ActionKeywords;
import config.Constants;
import utility.ExcelUtils;
import utility.ExtentManager;

import static config.Constants.*;
import static utility.ExtentManager.Description;

public class KeyWordDriven implements Runnable {
    private String sTCID;
    private int iTLS;
    private int sLoop;
    private int dataSet_Row;
    ExtentReports extent;
    private ExtentTest eReport;
    private ActionKeywords actionKeyword;


    private static ThreadLocal<ExtentTest> ex_Report = new ThreadLocal<ExtentTest>();


    public KeyWordDriven(String CaseId, int TestLastStep, int SuiteLoop, ExtentTest extent_Report,
                         String driver_Type, int dataSet_Row, ExtentReports extent) {
        sTCID = CaseId;
        iTLS = TestLastStep;
        sLoop = SuiteLoop;
        eReport = extent_Report;
        this.extent = extent;
        this.dataSet_Row = dataSet_Row;
        actionKeyword = new ActionKeywords();


    }


    @Override
    public void run() {
        try {
            execute_Case(iTLS, sTCID, sLoop, dataSet_Row, eReport);
            ex_Report.set(eReport);
            extent.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void execute_Case(int iTLS, String sTCID, int Suite_Loop, int dataSet_Row, ExtentTest extent)
            throws Exception {
        String Description;
        boolean isCase = true;
        for (int iTestStep = 1; iTestStep < iTLS; iTestStep++) {
            String sActionKeyword = ExcelUtils.getCellData(iTestStep, Constants.Col_ActionKeyword, sTCID);
            String sPageObject = ExcelUtils.getCellData(iTestStep, Constants.Col_PageObject, sTCID);
            String sD = ExcelUtils.getCellData(iTestStep, Constants.Col_DataSet, sTCID);
            String driver_Type = ExcelUtils.getCellData(Suite_Loop - 1, Col_DriverType, Sheet_TestSuites);
            Description = Description(sActionKeyword, sPageObject, iTestStep, sTCID, dataSet_Row, sD);
            if (isCase) {
                isCase = execute_Actions(sActionKeyword, sPageObject, sD, extent, Description);
                continue;
            }
            ExtentManager.Skip(eReport, Description);
        }
    }

    private boolean execute_Actions(String sActionKeyword, String sPageObject, String sD,
                                    ExtentTest extent, String Description) throws Exception {
        boolean isResult = false;

        isResult = (boolean) WebMethods.get(sActionKeyword).invoke(actionKeyword, sPageObject, sD, dataSet_Row, extent);
        if (isResult)
            ExtentManager.Pass(eReport, Description);
        else {
            ExtentManager.Fail(eReport, Description);
            WebMethods.get("closeBrowser").invoke(actionKeyword, sPageObject, sD, dataSet_Row, extent);
        }
        return isResult;
    }

}