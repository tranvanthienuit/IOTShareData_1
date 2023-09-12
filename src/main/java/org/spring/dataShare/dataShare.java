package org.spring.dataShare;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.dataShare.model.Address;
import org.spring.dataShare.model.DataSize;
import org.spring.dataShare.model.Item;
import org.spring.dataShare.model.SubItem;
import org.spring.dataShare.model.TypeSubItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class dataShare {
    public static final int COLUMN_ITEM_CHARACTER = 0;
    public static final int COLUMN_DATA_TYPE = 1;
    public static final int COLUMN_DATA_FORM = 2;
    public static final int COLUMN_DATA_SIZE = 3;
    public static final int COLUMN_ADDRESS = 4;
    public static final String PATH = "D:/set_up_IotDataSahre/";

    public static final String ISO = "ASCII";

    public static void main(String[] args) throws IOException {
        List<SubItem> subItems = getModels();
        Collections.sort(subItems);

        // convert dataSize if subItem have ASCII
        subItems.stream().peek(subItem -> {
            if (subItem.getDataForm() != null && subItem.getDataForm().equals(ISO)) {
                subItem.setDataSize(subItem.getItemCharacter());
            }
        }).collect(Collectors.toList());

        setItem(subItems);
    }

    private static List<Item> setItem(List<SubItem> subItems) {
        SubItem firstParent = subItems.get(0);
        List<Item> items = new ArrayList<>();
        items.add(
                Item.builder()
                        .parentItem(firstParent)
                        .subItems(List.of(firstParent))
                        .build()
        );

        for (SubItem subItem : subItems) {
            for (Item item : items) {

                int lastSubItemAddress = item.getSubItems().get(item.getSubItems().size() - 1).getAddress().getAddressItem();
                if (subItem.getAddress().getAddressItem() == lastSubItemAddress + DataSize.ONE.getValue()
                        && subItem.getDataSize() == DataSize.ONE.getValue()) {

                }
            }
        }
        return items;
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
                        subItem.setItemCharacter(((Double) getCellValue(cell)).intValue());
                        break;
                    case COLUMN_DATA_TYPE:
                        subItem.setDataType((String) getCellValue(cell));
                        break;
                    case COLUMN_DATA_FORM:
                        subItem.setDataForm((String) getCellValue(cell));
                        break;
                    case COLUMN_DATA_SIZE:
                        subItem.setDataSize(((Double) getCellValue(cell)).intValue());
                        break;
                    case COLUMN_ADDRESS:
                        subItem.setAddress(getAddress(cell));
                        break;
                    default:
                        break;
                }

            }
            subItem.setTypeSubItem(getTypeSubItem(subItem));

            subItems.add(subItem);
        }

        return subItems;
    }

    private static TypeSubItem getTypeSubItem(SubItem subItem) {
        if (Objects.equals(subItem.getDataForm(), ISO)) {
            return TypeSubItem.BSTR;
        }
        return switch (subItem.getDataSize()) {
            case 2 -> TypeSubItem.UI2;
            case 4 -> TypeSubItem.UI4;
            case 6, 8 -> TypeSubItem.UI8;
            default -> TypeSubItem.UI1;
        };
    }

    private static Address getAddress(Cell cell) {
        String cellItem = (String) getCellValue(cell);

        String domain = cellItem.lastIndexOf(".") == -1 ? cellItem : cellItem.substring(0, cellItem.lastIndexOf("."));
        Integer bit = cellItem.lastIndexOf(".") == -1 ? null : Integer.parseInt(cellItem.substring(cellItem.lastIndexOf(".") + 1));

        String variable = domain.replaceAll("[^A-Za-z]+", "");
        Integer addressItem = Integer.parseInt(domain.replaceAll("[^0-9.]", ""));

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
