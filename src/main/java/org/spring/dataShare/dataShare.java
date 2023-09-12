package org.spring.dataShare;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.dataShare.model.Address;
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
    public static final int COLUMN_DATA_FORM = 1;
    public static final int COLUMN_DATA_SIZE = 2;
    public static final int COLUMN_ADDRESS = 3;
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

        List<Item> items = setItem(subItems);

        System.out.println("Item Name,IsGroupMember,Item Enabled,Variable Enabled,Variable Name,Variable Option,Variable Class,Variable Object Name,Variable Object Option,Class Hierarchy,File Object Delimiter,Description Enabled,Description,Attribute,Link Item,Initialize Enabled,Initialize Data,Initialize Type,Force to Change,Use Empty,Deactivate,Use History,OnChange Events,Deadband Enabled,Deadbnad Min Value,Deadbnad Max Value,Filter Enabled,Filter Min Value,Filter Max Value,Chattering Enabled,Chattering Max Value,Mask Enabled,Mask Value,BCD Enabled,Request Enabled,Request Type,Request Enabled (Write),Request Type (Write),Allowed Min Value,Allowed Max Value,Allowed Step Value,Allowed List,Allowed Default,Misc Data Type,BLOB Size,BLOB Text,EU Unit,Compare Arrays,Extract Linking,Item Type,Link Property,Sub Item,Sub Item Block Size,Sub Item Block Update,Sub Item Parent Name,Sub Item Offset,Sub Item Data Type,Sub Item Unit Size,Sub Item Elem Count,Sub Item Byte Order,Direct Reading,Direct Reading Suppressed Time,Sub Item Offset Unit,Sub Item Bit,Sub Item Bit Position,Parent Item Data Type,Parent Item Unit Size,Parent Item Elem Count,Not Reconnect Judge Item,Server Publishing Setting");

        for (Item item : items) {
            calculateOffset(item);
            printItem(item);
        }
    }

    private static void printItem(Item item) {
        Address parentAddress = item.getParentItem().getAddress();
        String parentItem = parentAddress.getVariable() + parentAddress.getAddressItem();
        System.out.println("WordItem" + parentItem + ",False,True,True," + parentItem + ",'ELEM=" + item.getCount() + ",VT=Bit',0,,,False,\\,True,,1,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,False," + item.getCount() + ",0,,0,0,0,0,0,False,0,0,False,0,8209,1," + item.getCount() + ",False,2");
        if (item.getSubItems().size() == 1)
            return;
        for (SubItem subItem : item.getSubItems()) {
            Address address = subItem.getAddress();
            String addressSubItem = address.getBit() != null ? address.getVariable() + address.getAddressItem() + "-" + address.getBit() : address.getVariable() + address.getAddressItem();
            System.out.println("WordItem" + parentItem + "|" + addressSubItem + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + parentItem + "," + subItem.getOffset() + "," + subItem.getTypeSubItem().getValue() + ",1,1,1,False,0,0,False,0,0,0,0,False,2");
        }
    }

    private static Item calculateOffset(Item item) {
        int offset = 0;
        for (SubItem subItem : item.getSubItems()) {
            if (item.getSubItems().get(0).getOffset() == null) {
                subItem.setOffset(offset);
                continue;
            }
            offset = offset + subItem.getDataSize();
            subItem.setOffset(offset);
        }
        item.setCount(item.getSubItems().get(item.getSubItems().size() - 1).getOffset() / 2 + 1);
        return item;
    }

    private static List<Item> setItem(List<SubItem> subItems) {
        SubItem firstParent = subItems.get(0);
        List<Item> items = new ArrayList<>();
        Item currentItem = Item.builder()
                .parentItem(firstParent)
                .subItems(List.of(firstParent))
                .build();

        items.add(currentItem);

        for (SubItem subItem : subItems) {
            if (subItem.equals(items.get(0).getParentItem())) {
                continue;
            }
            currentItem = getCurrentItemAndAddSubItem(subItem, currentItem, items);
        }
        return items;
    }


    private static Item getCurrentItemAndAddSubItem(SubItem subItem, Item currentItem, List<Item> items) {

        SubItem lastSubItem = currentItem.getSubItems().get(currentItem.getSubItems().size() - 1);
        if (compareDataSize(lastSubItem, subItem)) {
            List<SubItem> newSubItem = new ArrayList<>(currentItem.getSubItems());
            newSubItem.add(subItem);
            currentItem.setSubItems(newSubItem);
            return currentItem;
        } else {
            Item item = Item.builder()
                    .parentItem(subItem)
                    .subItems(List.of(subItem))
                    .build();
            items.add(item);
            return item;
        }
    }

    private static boolean compareDataSize(SubItem lastSubItem, SubItem subItem) {
        if (!lastSubItem.getAddress().getVariable().equals(subItem.getAddress().getVariable())) {
            return false;
        }
        if (subItem.getAddress().getBit() != null
                && lastSubItem.getAddress().getBit() != null
                && lastSubItem.getAddress().getBit() + subItem.getDataSize() == subItem.getAddress().getBit()
                || lastSubItem.getAddress().getAddressItem() + subItem.getDataSize() == subItem.getAddress().getAddressItem()) {
            return true;
        }
        return subItem.getDataSize().equals(lastSubItem.getDataSize()) &&
                lastSubItem.getAddress().getAddressItem() + 1 == subItem.getAddress().getAddressItem();
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
            case 0, 1 -> TypeSubItem.UI1;
            case 2 -> TypeSubItem.UI2;
            case 3, 4 -> TypeSubItem.UI4;
            default -> TypeSubItem.UI8;
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
