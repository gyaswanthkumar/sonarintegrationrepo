package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import executionEngine.DriverScript;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import config.Constants;

import static config.Constants.OR;
import static config.Constants.Sheet_TestSuites;

public class ExcelUtils {
    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static org.apache.poi.ss.usermodel.Cell Cell;
    private static XSSFRow Row;

    public ExcelUtils() throws IOException {
    }
    //private static XSSFRow Row;

    public static void setExcelFile(String Path) throws Exception {
        try {
            FileInputStream ExcelFile = new FileInputStream(Path);
            ExcelWBook = new XSSFWorkbook(ExcelFile);
        } catch (Exception e) {

            DriverScript.bResult = false;
        }
    }

    public static int getRowCount(String SheetName) {
        int iNumber = 0;
        try {
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            iNumber = ExcelWSheet.getLastRowNum() + 1;
        } catch (Exception e) {
               DriverScript.bResult = false;
        }
        return iNumber;
    }

    public static int getRowContains(String sTestCaseName, int colNum, String SheetName) throws Exception {
        int iRowNum = 0;
        try {
            //ExcelWSheet = ExcelWBook.getSheet(SheetName);
            int rowCount = ExcelUtils.getRowCount(SheetName);
            for (; iRowNum < rowCount; iRowNum++) {
                if (ExcelUtils.getCellData(iRowNum, colNum, SheetName).equalsIgnoreCase(sTestCaseName)) {
                    break;
                }
            }
        } catch (Exception e) {
                       DriverScript.bResult = false;
        }
        return iRowNum;
    }

    public static String getCellData(int RowNum, int ColNum, String SheetName) throws Exception {
        try {
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
            String CellData = Cell.getStringCellValue();
            return CellData;
        } catch (Exception e) {

            DriverScript.bResult = false;
            return "";
        }
    }

    public static int getCasesCount(String SheetName, int Row, int Col) throws Exception {
        int number = 0;
        try {
            for (int i = Row; i <= ExcelUtils.getRowCount(SheetName); i++) {
                if (!ExcelUtils.getCellData(i, Col, SheetName).isEmpty()) {
                    number++;
                } else
                    break;
            }
            return number;

        } catch (Exception e) {

            DriverScript.bResult = false;
            return 0;
        }
    }

    public static int getTestStepCount(String SheetName, String sTestCaseID, int iTestCaseStart) throws Exception {
        try {
            for (int i = iTestCaseStart; i <= ExcelUtils.getRowCount(SheetName); i++) {
                if (!sTestCaseID.equals(ExcelUtils.getCellData(i, Constants.Col_TestCaseID, SheetName))) {
                    int number = i;
                    return number;
                }
            }
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            int number = ExcelWSheet.getLastRowNum() + 1;
            return number;
        } catch (Exception e) {

            DriverScript.bResult = false;
            return 0;
        }
    }

    @SuppressWarnings("static-access")
    public static void setCellData(String Result, int RowNum, int ColNum, String SheetName) throws Exception {
        try {
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            Row = ExcelWSheet.getRow(RowNum);
            Cell = Row.getCell(ColNum, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (Cell == null) {
                Cell = Row.createCell(ColNum);
                Cell.setCellValue(Result);
            } else {
                Cell.setCellValue(Result);
            }
            FileOutputStream fileOut = new FileOutputStream(OR.getProperty("Path_TestData"));
            ExcelWBook.write(fileOut);
            //fileOut.flush();
            fileOut.close();
            ExcelWBook = new XSSFWorkbook(new FileInputStream(OR.getProperty("Path_TestData")));
        } catch (Exception e) {
            DriverScript.bResult = false;
        }
    }


    public static List<Map<String, String>> getData(String excelFilePath, String sheetName)
            throws InvalidFormatException, IOException {
        Sheet sheet = getSheetByName(excelFilePath, sheetName);
        return readSheet(sheet);
    }

    public List<Map<String, String>> getData(String excelFilePath, int sheetNumber)
            throws InvalidFormatException, IOException {
        Sheet sheet = getSheetByIndex(excelFilePath, sheetNumber);
        return readSheet(sheet);

    }

    private static Sheet getSheetByName(String excelFilePath, String sheetName) throws IOException, InvalidFormatException {
        Sheet sheet = getWorkBook(excelFilePath).getSheet(sheetName);
        return sheet;
    }

    private Sheet getSheetByIndex(String excelFilePath, int sheetNumber) throws IOException, InvalidFormatException {
        Sheet sheet = getWorkBook(excelFilePath).getSheetAt(sheetNumber);
        return sheet;
    }

    private static Workbook getWorkBook(String excelFilePath) throws IOException, InvalidFormatException, IOException {
        return WorkbookFactory.create(new File(excelFilePath));
    }

    private static List<Map<String, String>> readSheet(Sheet sheet) {
        org.apache.poi.ss.usermodel.Row row;
        int totalRow = sheet.getPhysicalNumberOfRows();
        List<Map<String, String>> excelRows = new ArrayList<Map<String, String>>();
        int headerRowNumber = getHeaderRowNumber(sheet);
        if (headerRowNumber != -1) {
            int totalColumn = sheet.getRow(headerRowNumber).getLastCellNum();
            int setCurrentRow = 1;
            for (int currentRow = setCurrentRow; currentRow <= totalRow; currentRow++) {
                row = getRow(sheet, sheet.getFirstRowNum() + currentRow);
                LinkedHashMap<String, String> columnMapdata = new LinkedHashMap<String, String>();
                for (int currentColumn = 0; currentColumn < totalColumn; currentColumn++) {
                    columnMapdata.putAll(getCellValue(sheet, row, currentColumn));
                }
                excelRows.add(columnMapdata);
            }
        }
        return excelRows;
    }

    private static int getHeaderRowNumber(Sheet sheet) {
        Row row;
        int totalRow = sheet.getLastRowNum();
        for (int currentRow = 0; currentRow <= totalRow + 1; currentRow++) {
            row = getRow(sheet, currentRow);
            if (row != null) {
                int totalColumn = row.getLastCellNum();
                for (int currentColumn = 0; currentColumn < totalColumn; currentColumn++) {
                    org.apache.poi.ss.usermodel.Cell cell;
                    cell = row.getCell(currentColumn, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (cell.getCellType() == CellType.STRING) {
                        return row.getRowNum();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        return row.getRowNum();

                    } else if (cell.getCellType() == CellType.BOOLEAN) {
                        return row.getRowNum();
                    } else if (cell.getCellType() == CellType.ERROR) {
                        return row.getRowNum();
                    }
                }
            }
        }
        return (-1);
    }

    private static Row getRow(Sheet sheet, int rowNumber) {
        return sheet.getRow(rowNumber);
    }

    private static LinkedHashMap<String, String> getCellValue(Sheet sheet, Row row, int currentColumn) {
        LinkedHashMap<String, String> columnMapdata = new LinkedHashMap<String, String>();
        Cell cell;
        if (row == null) {
            if (sheet.getRow(sheet.getFirstRowNum()).getCell(currentColumn, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                    .getCellType() != CellType.BLANK) {
                String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(currentColumn)
                        .getStringCellValue();
                columnMapdata.put(columnHeaderName, "");
            }
        } else {
            cell = row.getCell(currentColumn, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell.getCellType() == CellType.STRING) {
                if (sheet.getRow(sheet.getFirstRowNum())
                        .getCell(cell.getColumnIndex(), org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, cell.getStringCellValue());
                }
            } else if (cell.getCellType() == CellType.NUMERIC) {
                if (sheet.getRow(sheet.getFirstRowNum())
                        .getCell(cell.getColumnIndex(), org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, NumberToTextConverter.toText(cell.getNumericCellValue()));
                }
            } else if (cell.getCellType() == CellType.BLANK) {
                if (sheet.getRow(sheet.getFirstRowNum())
                        .getCell(cell.getColumnIndex(), org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, "");
                }
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                if (sheet.getRow(sheet.getFirstRowNum())
                        .getCell(cell.getColumnIndex(), org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, Boolean.toString(cell.getBooleanCellValue()));
                }
            } else if (cell.getCellType() == CellType.ERROR) {
                if (sheet.getRow(sheet.getFirstRowNum())
                        .getCell(cell.getColumnIndex(), org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                        .getCellType() != CellType.BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, Byte.toString(cell.getErrorCellValue()));
                }
            }
        }
        return columnMapdata;
    }

    public static List<Integer> split(int Row, int Col) throws Exception {
        String s = ExcelUtils.getCellData(Row, Col, Sheet_TestSuites);
        int EntireRow = Constants.data.size();
        if (s.contains(":") && !s.contains(",")) {
            String[] s1 = s.split(":");
            try {
                return Range_Of_Data(Integer.valueOf(s1[0]) - 1, Integer.valueOf(s1[1]));
            } catch (Exception e) {
                try {
                    return Range_Of_Data(Integer.valueOf(s1[0]) - 1, EntireRow - 1);
                } catch (Exception f) {
                    try {
                        return Range_Of_Data(0, Integer.valueOf(s1[1]));
                    } catch (Exception g) {
                        return Range_Of_Data(0, EntireRow - 1);
                    }
                }
            }
        } else if (s.contains(",") && s.contains(":")) {
            List<Integer> SetOfData = new ArrayList<>();
            String[] s1 = s.split(",");
            for (String s2 : s1) {
                if (s2.contains(":")) {
                    String[] s3 = s2.split(":");
                    try {
                        SetOfData.addAll(Range_Of_Data(Integer.valueOf(s3[0]) - 1, Integer.valueOf(s3[1])));
                    } catch (Exception g) {
                        SetOfData.addAll(Range_Of_Data(Integer.valueOf(s3[0]) - 1, EntireRow - 1));
                    }
                } else
                    SetOfData.add(Integer.parseInt(s2) - 1);
            }
            return SetOfData;
        } else if (s.contains(",") && !s.contains(":")) {
            String[] s1 = s.split(",");
            List<String> SetOfData = Arrays.asList(s1);
            List<Integer> newList = SetOfData.stream()
                    .map(k -> Integer.parseInt(k) - 1)
                    .collect(Collectors.toList());
            return newList;
        }
        return Range_Of_Data(Integer.valueOf(s) - 1, Integer.valueOf(s));
    }

    public static List<Integer> Range_Of_Data(int InitalValue, int FinalValue) throws Exception {
        List<Integer> fixedLenghtList = new ArrayList<>();
        for (int i = InitalValue; i < FinalValue; i++) {
            fixedLenghtList.add(i);
        }
        return fixedLenghtList;
    }

    public static String [] split(String BrowserCount){
        String S[]=BrowserCount.split(",");
       if (S.length==2) {
            return new String[]{S[0],S[1]};
        }
        else if(S.length==3){
            return new String[]{S[0],S[1],S[2]};
        }
        return new String[]{S[0]};
    }
}