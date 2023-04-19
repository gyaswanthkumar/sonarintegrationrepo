package config;

import com.aventstack.extentreports.ExtentTest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Constants {
    public static String ExcelPath =".\\src\\main\\resources\\LatestDataEngine.xlsx";
    public static String Path_OR=".\\src\\main\\resources\\Page Object.txt";
    public static final String Web_Method = "Web";

    public static Integer COUNT =1000;

    //Data Sheet Column Numbers
    public static final int Col_TestSuite = 0;
    public static final int Col_TestCaseID = 1;
    public static final int Col_PageObject = 2;
    public static final int Col_ActionKeyword = 3;
    public static final int Col_RunMode = 4;
    public static final int Col_ParallelMode = 6;
    public static final int Col_DataSet = 4;
    public static final int Col_DriverType = 5;


    // Data Engine Excel sheets
    public static final String Sheet_TestSuites = "Suites";
    public static final String Sheet_DataSet = "Test Data";
    public static final String Sheet_MobileData = "SalesForce";


    // DriverScript Variables
    public static Properties OR;
    public static ActionKeywords actionKeywords;

    public static String driver_Type;

    public static int iTestLastStep;
    public static String sTestCaseID;
    public static String sRunMode;
    public static String sData;
    public static int TotalCases;
    public static int DataSet_Row;
    public static ExtentTest ExtentReportStatus;
    public static List<Map<String, String>> data;
    public static List<Map<String, String>> MobileData;
    public static HashMap<String, Method> WebMethods = new HashMap<>();
    public static HashMap<String, Method> MobileMethods = new HashMap<>();

    //Jira Variables
    public static final String Jsummary = "Bug Arised from Test Case ID: ";

    public static final String Jdescription = ", Following Step Has Been Impeded To Complete The Test" +
            " Scenario And For Further Details Please Check the Following Attachment";
}

