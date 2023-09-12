package org.spring.dataShare;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.dataShare.model.Address;
import org.spring.dataShare.model.SubItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class dataShare {
    public static final int COLUMN_ITEM_CHARACTER = 0;
    public static final int COLUMN_DATA_TYPE = 1;
    public static final int COLUMN_DATA_FORM = 2;
    public static final int COLUMN_DATA_SIZE = 3;
    public static final int COLUMN_ADDRESS = 4;
    public static final String PATH = "D:/set_up_IotDataSahre/";

    public static void main(String[] args) throws IOException {
        List<SubItem> subItems = getModels();
        Collections.sort(subItems);
        for (SubItem subItem : subItems) {
            subItem.toString();
        }
    }

    private static List<SubItem> getModels() throws IOException {
        FileInputStream file = new FileInputStream(new File(PATH + "data.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        List<SubItem> subItems = new ArrayList<>();

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
            SubItem subItem = new SubItem();
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);

                // Set value for model object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_ITEM_CHARACTER:
                        subItem.setItemCharacter((String) getCellValue(cell));
                        break;
                    case COLUMN_DATA_TYPE:
                        subItem.setDataType((String) getCellValue(cell));
                        break;
                    case COLUMN_DATA_FORM:
                        subItem.setDataForm((String) getCellValue(cell));
                        break;
                    case COLUMN_DATA_SIZE:
                        subItem.setDataSize((String) getCellValue(cell));
                        break;
                    case COLUMN_ADDRESS:
                        subItem.setAddress(getAddress(cell));
                        break;
                    default:
                        break;
                }

            }
            subItems.add(subItem);
        }

        return subItems;
    }

    private static Address getAddress(Cell cell) {
        String cellItem = (String) getCellValue(cell);

        String domain = cellItem.lastIndexOf(".") == -1 ? cellItem : cellItem.substring(0, cellItem.lastIndexOf("."));
        String bit = cellItem.lastIndexOf(".") == -1 ? null : cellItem.substring(cellItem.lastIndexOf("."));

        String variable = domain.replaceAll("[^A-Za-z]+", "");
        String addressItem = domain.replaceAll("[^0-9.]", "");

        return Address.builder()
                .variable(variable)
                .addressItem(addressItem)
                .bit(bit)
                .build();
    }

    // Get cell value
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
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
