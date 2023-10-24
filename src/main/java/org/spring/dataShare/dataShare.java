package org.spring.dataShare;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.dataShare.model.Address;
import org.spring.dataShare.model.Item;
import org.spring.dataShare.model.SubItem;
import org.spring.dataShare.model.TypeSubItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class dataShare {
    public static final int COLUMN_ITEM_CHARACTER = 0;
    public static final int COLUMN_DATA_FORM = 1;
    public static final int COLUMN_DATA_SIZE = 2;
    public static final int COLUMN_ADDRESS = 3;
    public static final String PATH = "D:/REP_DP_IOT/";

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

        int elementCount = 0;
        for (Item item : items) {
            calculateOffset(item);
            elementCount = printItem(item) + elementCount;
        }
        System.out.println("Total element: " + elementCount);
    }

    private static int printItem(Item item) {
        int elementCount = 0;
        Address parentAddress = item.getParentItem().getAddress();
        String parentItem = parentAddress.getVariable() + parentAddress.getAddressItem();
        System.out.println("WordItem" + parentItem + ",False,True,True," + parentItem + ",'ELEM=" + item.getCount() + ",VT=Bit',0,,,False,\\,True,,1,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,False," + item.getCount() + ",0,,0,0,0,0,0,False,0,0,False,0,8209,1," + item.getCount() + ",False,2");
        if (item.getSubItems().size() == 1)
            return 1;
        for (SubItem subItem : item.getSubItems()) {
            elementCount++;
            Address address = subItem.getAddress();
            String addressSubItem = address.getBit() != null ? address.getVariable() + address.getAddressItem() + "-" + address.getBit() : address.getVariable() + address.getAddressItem();
            System.out.println("WordItem" + parentItem + "|" + addressSubItem + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + parentItem + "," + subItem.getOffset() + "," + subItem.getTypeSubItem().getValue() + "," + getSubItemUnitSize(subItem) + ",1,1,False,0,0,False,0,0,0,0,False,2");
        }
        return elementCount;
    }

    private static Integer getSubItemUnitSize(SubItem subItem) {
        switch (subItem.getTypeSubItem()) {
            case BSTR -> {
                return subItem.getDataSize();
            }
            case UI2 -> {
                return 2;
            }
            case UI4 -> {
                return 4;
            }
            case UI8 -> {
                return 8;
            }
            default -> {
                return 1;
            }
        }
    }

    private static Item calculateOffset(Item item) {
        int offset = 0;
        List<SubItem> subItems = item.getSubItems();
        for (int i = 0; i < subItems.size(); i++) {
            SubItem subItem = subItems.get(i);

            if (subItems.get(0).getOffset() == null) {
                subItem.setOffset(offset);
                continue;
            }
            SubItem previousSubItem = subItems.get(i - 1);
            if (compareAddressItem(previousSubItem.getAddress().getAddressItem(), subItem.getAddress().getAddressItem())) {
                offset = offset + previousSubItem.getTypeSubItem().getOffset();
            }
            subItem.setOffset(offset);
        }

        if (item.getSubItems().get(item.getSubItems().size() - 1).getOffset() == 0) {
            if (item.getSubItems().get(item.getSubItems().size() - 1).getDataSize() % 2 != 0) {
                item.setCount(item.getSubItems().get(item.getSubItems().size() - 1).getDataSize() / 2 + 1);
            } else {
                item.setCount(item.getSubItems().get(item.getSubItems().size() - 1).getDataSize() / 2);
            }
            return item;
        }


        if (item.getSubItems().get(item.getSubItems().size() - 1).getOffset().doubleValue() % 2 != 0) {
            item.setCount(item.getSubItems().get(item.getSubItems().size() - 1).getOffset() / 2);
        } else {
            item.setCount(item.getSubItems().get(item.getSubItems().size() - 1).getOffset() / 2 + 1);
        }
        return item;
    }

    private static boolean compareAddressItem(String s1, String s2) {
        // both number
        if (StringUtils.isNumeric(s1) && StringUtils.isNumeric(s2)) {
            return Integer.parseInt(s1) < Integer.parseInt(s2);
        }

        Integer s1Number = 0, s2Number = 0;
        String s1Character = "", s2Character = "";

        if (!StringUtils.isNumeric(s1) && !StringUtils.isNumeric(s2)) {
            s1Number = Integer.parseInt(s1.replaceAll("[^0-9.]", ""));
            s1Character = s1.replaceAll("[^A-Za-z]+", "");

            s2Number = Integer.parseInt(s2.replaceAll("[^0-9.]", ""));
            s2Character = s2.replaceAll("[^A-Za-z]+", "");
        }

        if (StringUtils.isNumeric(s1) && !StringUtils.isNumeric(s2)) {
            s2Number = Integer.parseInt(s2.replaceAll("[^0-9.]", ""));
            s2Character = s2.replaceAll("[^A-Za-z]+", "");

            s1Number = Integer.parseInt(s1.substring(0, String.valueOf(s2Number).length()));
            s1Character = s1.substring(String.valueOf(s2Number).length());
        }

        if (!StringUtils.isNumeric(s1) && StringUtils.isNumeric(s2)) {
            s1Number = Integer.parseInt(s2.replaceAll("[^0-9.]", ""));
            s1Character = s2.replaceAll("[^A-Za-z]+", "");

            s2Number = Integer.parseInt(s2.substring(0, String.valueOf(s1Number).length()));
            s2Character = s1.substring(String.valueOf(s1Number).length());
        }

        if (s1Number.equals(s2Number)) {
            return (StringUtils.isNumeric(s1Character) ? Integer.parseInt(s1Character) : getBit(s1Character)) < (StringUtils.isNumeric(s2Character) ? Integer.parseInt(s2Character) : getBit(s2Character));
        }
        return s1Number < s2Number;
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

        for (SubItem subItem : subItems) {
            if (subItem.getAddress().getBit() != null
                    && ((StringUtils.isNumeric(subItem.getAddress().getBit()) && Integer.parseInt(subItem.getAddress().getBit()) > 7)
                    || (!StringUtils.isNumeric(subItem.getAddress().getBit()) && getBit(subItem.getAddress().getBit()) > 7))) {
                for (SubItem subItem1 : subItems) {
                    if (Objects.equals(subItem1.getAddress().getAddressItem(), subItem.getAddress().getAddressItem())) {
                        subItem1.setTypeSubItem(TypeSubItem.UI2);
                    }
                }
            }
        }
        return items;
    }


    private static Item getCurrentItemAndAddSubItem(SubItem subItem, Item currentItem, List<Item> items) {

        SubItem lastSubItem = currentItem.getSubItems().get(currentItem.getSubItems().size() - 1);
        if (compareSubItem(lastSubItem, subItem)) {
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

    private static boolean compareSubItem(SubItem lastSubItem, SubItem subItem) {
        if (!lastSubItem.getAddress().getVariable().equals(subItem.getAddress().getVariable())) {
            return false;
        }
        if (subItem.getAddress().getBit() != null
                && lastSubItem.getAddress().getBit() != null
                && checkBitData(lastSubItem.getAddress().getBit(), subItem.getAddress().getBit(), lastSubItem.getDataSize())
                || checkAddressData(lastSubItem.getAddress().getAddressItem(), subItem.getAddress().getAddressItem(), lastSubItem.getDataSize())
                || lastSubItem.getAddress().getAddressItem().equals(subItem.getAddress().getAddressItem())) {
            return true;
        }
        return subItem.getDataSize().equals(lastSubItem.getDataSize()) &&
                checkAddressData(lastSubItem.getAddress().getAddressItem(), subItem.getAddress().getAddressItem(), 1);
    }

    private static boolean checkAddressData(String s1, String s2, Integer lastDataSize) {
        // both number
        if (StringUtils.isNumeric(s1) && StringUtils.isNumeric(s2) && Integer.parseInt(s1) + lastDataSize == Integer.parseInt(s2))
            return true;
        //both character
        if (!StringUtils.isNumeric(s1) && !StringUtils.isNumeric(s2)) {

            Integer s1Number = Integer.parseInt(s1.replaceAll("[^0-9.]", ""));
            String s1Character = s1.replaceAll("[^A-Za-z]+", "");

            Integer s2Number = Integer.parseInt(s2.replaceAll("[^0-9.]", ""));
            String s2Character = s2.replaceAll("[^A-Za-z]+", "");

            if (s1Number.equals(s2Number) && getBit(s1Character) + lastDataSize == getBit(s2Character))
                return true;
        }
        // s2 have character
        if (StringUtils.isNumeric(s1) && !StringUtils.isNumeric(s2)) {

            int s2Number = Integer.parseInt(s2.replaceAll("[^0-9.]", ""));
            String s2Character = s2.replaceAll("[^A-Za-z]+", "");


            int s1Number = Integer.parseInt(s1.substring(0, String.valueOf(s2Number).length()));
            int s1Character = Integer.parseInt(s1.substring(String.valueOf(s2Number).length()));

            return s1Number == s2Number && s1Character + lastDataSize == getBit(s2Character);
        }
        return false;
    }

    private static boolean checkBitData(String s1, String s2, Integer lastDataSize) {
        if (StringUtils.isNumeric(s1) && StringUtils.isNumeric(s2) && Integer.parseInt(s1) + lastDataSize == Integer.parseInt(s2))
            return true;
        if (!StringUtils.isNumeric(s1) && !StringUtils.isNumeric(s2) && getBit(s1) + lastDataSize == getBit(s2))
            return true;
        return StringUtils.isNumeric(s1) && !StringUtils.isNumeric(s2) && Integer.parseInt(s1) + lastDataSize == getBit(s2);
    }

    static Integer getBit(String bit) {
        return switch (bit) {
            case "A" -> 10;
            case "B" -> 11;
            case "C" -> 12;
            case "D" -> 13;
            case "E" -> 14;
            case "F" -> 15;
            default -> null;
        };
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
        String bit = cellItem.lastIndexOf(".") == -1 ? null : cellItem.substring(cellItem.lastIndexOf(".") + 1);

        String variable = domain.replaceAll("[^A-Za-z]+", "");
        if (variable.length() > 1) {
            variable = domain.substring(0, 1);
        }
        String addressItem = domain.substring(variable.length());

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
