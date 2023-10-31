package org.spring.dummy;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.dummy.model.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Dummy {
    public static final int COLUMN_ITEM_NAME = 0;
    public static final int COLUMN_ITEM_VARIABLE_OPTION = 5;
    public static final String PATH = "D:/";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input controller's name: ");
        String fileName = scanner.nextLine();
        List<Item> items = getModels(fileName);
        List<Item> newItem = new ArrayList<>(items);
        for (int i = 0; i < items.size(); i++) {
            if (i + 1 < items.size() && items.get(i + 1).getElement() != null) {
                newItem.remove(items.get(i));
            }
        }
        newItem = newItem.stream().filter(item -> item.getElement() != null).toList();
        printController(fileName, newItem);
    }

    static void printController(String controllerName, List<Item> items) {
        System.out.println("Item Name,IsGroupMember,Item Enabled,Variable Enabled,Variable Name,Variable Option,Variable Class,Variable Object Name,Variable Object Option,Class Hierarchy,File Object Delimiter,Description Enabled,Description,Attribute,Link Item,Initialize Enabled,Initialize Data,Initialize Type,Force to Change,Use Empty,Deactivate,Use History,OnChange Events,Deadband Enabled,Deadband Min Value,Deadband Max Value,Filter Enabled,Filter Min Value,Filter Max Value,Chattering Enabled,Chattering Max Value,Mask Enabled,Mask Value,BCD Enabled,Request Enabled,Request Type,Request Enabled (Write),Request Type (Write),Allowed Min Value,Allowed Max Value,Allowed Step Value,Allowed List,Allowed Default,Misc Data Type,BLOB Size,BLOB Text,EU Unit,Compare Arrays,Extract Linking,Item Type,Link Property,Sub Item,Sub Item Block Size,Sub Item Block Update,Sub Item Parent Name,Sub Item Offset,Sub Item Data Type,Sub Item Unit Size,Sub Item Elem Count,Sub Item Byte Order,Direct Reading,Direct Reading Suppressed Time,Sub Item Offset Unit,Sub Item Bit,Sub Item Bit Position,Parent Item Data Type,Parent Item Unit Size,Parent Item Elem Count,Not Reconnect Judge Item,Server Publishing Setting");
        int begin = 0;
        for (Item item : items) {
            System.out.println(item.getItemName() + ",False,True,True,D" + begin + ",Elem=" + item.getElement() + ",0,,,False,\\,True,,1," + controllerName + "\\" + item.getItemName() + ",False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,False,0,0,,0,0,0,0,0,False,0,0,False,0,0,0,0,False,2");
            begin = begin + item.getElement();
        }
    }

    private static List<Item> getModels(String fileName) throws IOException {
        FileInputStream file = new FileInputStream(new File(PATH + fileName + ".xlsx"));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        List<Item> items = new ArrayList<>();

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
            Item item = new Item();
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();
                Object cellValue = getCellValue(cell);

                // Set value for model object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_ITEM_NAME:
                        item.setItemName(((String) getCellValue(cell)));
                        break;
                    case COLUMN_ITEM_VARIABLE_OPTION:
                        item.setElement(getElement((String) getCellValue(cell)));
                        break;
                    default:
                        break;
                }
            }
            items.add(item);
        }

        return items;
    }

    private static Integer getElement(String variableOption) {
        if (variableOption.equals(""))
            return null;
        String variableElement = StringUtils.substringAfter(variableOption, "Elem=");
        return variableElement.lastIndexOf(",") == -1 ? Integer.parseInt(variableElement) : Integer.parseInt(variableElement.substring(0, variableElement.indexOf(",")));
    }

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
