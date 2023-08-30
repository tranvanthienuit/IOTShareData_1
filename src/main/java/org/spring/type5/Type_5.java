package org.spring.type5;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Type_5 {
    public static final int COLUMN_INDEX_DIGIT = 0;
    public static final int COLUMN_INDEX_DATA_FORM = 1;
    public static final int COLUMN_INDEX_SIZE = 2;
    public static final int COLUMN_INDEX_ADDRESS = 3;

    public static void main(String[] args) throws IOException {
        List<Model> models = getModels();
        Model parent = models.get(0);

        System.out.println("Item's Name: " + parent.getAddress());

        System.out.println("Item Name,IsGroupMember,Item Enabled,Variable Enabled,Variable Name,Variable Option,Variable Class,Variable Object Name,Variable Object Option,Class Hierarchy,File Object Delimiter,Description Enabled,Description,Attribute,Link Item,Initialize Enabled,Initialize Data,Initialize Type,Force to Change,Use Empty,Deactivate,Use History,OnChange Events,Deadband Enabled,Deadbnad Min Value,Deadbnad Max Value,Filter Enabled,Filter Min Value,Filter Max Value,Chattering Enabled,Chattering Max Value,Mask Enabled,Mask Value,BCD Enabled,Request Enabled,Request Type,Request Enabled (Write),Request Type (Write),Allowed Min Value,Allowed Max Value,Allowed Step Value,Allowed List,Allowed Default,Misc Data Type,BLOB Size,BLOB Text,EU Unit,Compare Arrays,Extract Linking,Item Type,Link Property,Sub Item,Sub Item Block Size,Sub Item Block Update,Sub Item Parent Name,Sub Item Offset,Sub Item Data Type,Sub Item Unit Size,Sub Item Elem Count,Sub Item Byte Order,Direct Reading,Direct Reading Suppressed Time,Sub Item Offset Unit,Sub Item Bit,Sub Item Bit Position,Parent Item Data Type,Parent Item Unit Size,Parent Item Elem Count,Not Reconnect Judge Item,Server Publishing Setting\n");
        int offset = 0;

        for (Model model : models) {
            System.out.println("WordItem" + parent.getAddress() + "|" + model.getAddress() + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + model.getAddress() + "," + offset + "," + getDataType(model).getValue() + "," + getDataSize(model) + ",1,1,False,0,0,False,0,0,0,0,False,2\n");
            offset = offset + 2;
        }
    }

    private static int getDataSize(Model model) {
        return switch (getDataType(model)) {
            case UI1 -> 1;
            case UI2 -> 2;
            case UI4 -> 4;
            case UI8 -> 8;
            default -> model.getDigit().intValue();
        };
    }

    private static DataType getDataType(Model model) {
        if (model.getSize().intValue() == 1)
            return getType(model, DataType.UI1);
        if (model.getSize().intValue() == 2)
            return getType(model, DataType.UI2);
        if (model.getSize().intValue() <= 4 && model.getSize().intValue() > 2)
            return getType(model, DataType.UI4);
        if (model.getSize().intValue() <= 8 && model.getSize().intValue() > 4)
            return getType(model, DataType.UI8);
        return DataType.BSTR;
    }

    private static DataType getType(Model model, DataType dataType) {
        return Objects.equals(model.getDataForm(), "ASCII") ? DataType.BSTR : dataType;
    }

    private static List<Model> getModels() throws IOException {
        FileInputStream file = new FileInputStream(new File("D:/inputSheet.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        List<Model> models = new ArrayList<>();

        // Get all rows
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            if (nextRow.getRowNum() == 0) {
                // Ignore header
                continue;
            }

            // Get all cells
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            // Read cells and set value for model object
            Model model = new Model();
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);

                // Set value for model object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_INDEX_DIGIT:
                        model.setDigit((Double) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_SIZE:
                        model.setSize((Double) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_DATA_FORM:
                        model.setDataForm((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_ADDRESS:
                        model.setAddress((String) getCellValue(cell));
                        break;
                    default:
                        break;
                }

            }
            models.add(model);
        }

        return models;
    }

    // Get cell value
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            default:
                break;
        }

        return cellValue;
    }
}
