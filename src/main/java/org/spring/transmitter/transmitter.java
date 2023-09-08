package org.spring.transmitter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class transmitter {
    public static final int COLUMN_INDEX_TYPE = 0;
    public static final int COLUMN_INDEX_TIMING = 1;
    public static final int COLUMN_INDEX_SIGN_ID = 2;
    public static final int COLUMN_INDEX_ADDRESS = 3;
    public static final int COLUMN_INDEX_SUB_ITEM = 5;

    public static final String PATH = "D:/set_up_IotDataSahre/newproject/transmitter/";

    public static void main(String[] args) throws IOException, IllegalAccessException {
        System.out.println("Input file: ");
        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();

        List<ModelTransmitter> modelTransmitters = getTransmitter(file);

        writeExel(modelTransmitters, file);
    }

    private static void writeExel(List<ModelTransmitter> modelTransmitters, String file) throws IOException, IllegalAccessException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("data");

        Row header = sheet.createRow(0);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Type");

        headerCell = header.createCell(1);
        headerCell.setCellValue("Timing");

        headerCell = header.createCell(2);
        headerCell.setCellValue("SignID");

        headerCell = header.createCell(3);
        headerCell.setCellValue("Address");

        headerCell = header.createCell(4);
        headerCell.setCellValue("Item");

        headerCell = header.createCell(5);
        headerCell.setCellValue("SubItem");

        int rowid = 1;

        for (ModelTransmitter modelTransmitter : modelTransmitters) {

            XSSFRow row = (XSSFRow) sheet.createRow(rowid++);
            Field[] fields = modelTransmitter.getClass().getDeclaredFields();
            int cellid = 0;

            for (Field field : fields) {
                field.setAccessible(true);
                Cell cell;
                if (field.get(modelTransmitter) == CellType.BLANK.name()) {
                    cell = row.createCell(cellid++, CellType.BLANK);
                } else {
                    cell = row.createCell(cellid++);
                    cell.setCellValue(String.valueOf(field.get(modelTransmitter)));
                }
            }
        }

        FileOutputStream out = new FileOutputStream(
                new File(PATH + "exel/" + file + ".xlsx"));

        workbook.write(out);
        out.close();
    }

    private static List<ModelTransmitter> getTransmitter(String file) throws IOException {
        List<ModelTransmitter> modelTransmitters = new ArrayList<>();
        List<ModelItem> modelItems = getOutPuts(file);

        for (ModelTransmitter modelTransmitter : getModels(file)) {
            String subItem;
            String item;
            if (modelTransmitter.getAddress().lastIndexOf(".") == -1) {
                String parentItem = getParentItem(modelTransmitter.getAddress(), modelItems);
                if (parentItem != null) {
                    item = parentItem;
                    subItem = modelTransmitter.getAddress();
                } else {
                    item = "WordItem" + modelTransmitter.getAddress();
                    subItem = CellType.BLANK.name();
                }
            } else {
                String bitCharacter = modelTransmitter.getAddress().substring(modelTransmitter.getAddress().lastIndexOf(".") + 1);

                int bit = getBit(bitCharacter) == null ? Integer.parseInt(modelTransmitter.getAddress().substring(modelTransmitter.getAddress().lastIndexOf(".") + 1)) : getBit(bitCharacter);

                subItem = modelTransmitter.getAddress().substring(0, modelTransmitter.getAddress().lastIndexOf(".")) + "-" + bit;

                item = getParentItem(subItem, modelItems);
            }


            ModelTransmitter model = ModelTransmitter.builder()
                    .type(modelTransmitter.getType())
                    .timing(modelTransmitter.getTiming())
                    .signID(modelTransmitter.getSignID())
                    .address(modelTransmitter.getAddress())
                    .item(item)
                    .subItem(subItem)
                    .build();
            modelTransmitters.add(model);
        }
        return modelTransmitters;
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

    private static String getParentItem(String subItem, List<ModelItem> modelItems) {
        for (ModelItem modelItem : modelItems) {
            if (Objects.equals(modelItem.getSubItem(), subItem))
                return modelItem.getParentName();
        }
        return null;
    }

    // get input
    private static List<ModelTransmitter> getModels(String fileName) throws IOException {
        FileInputStream file = new FileInputStream(new File(PATH + "input/" + fileName + ".xlsx"));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        List<ModelTransmitter> ModelTransmitter = new ArrayList<>();

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
            ModelTransmitter model = new ModelTransmitter();
            while (cellIterator.hasNext()) {
                //Read cell
                Cell cell = cellIterator.next();

                // Set value for model object
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case COLUMN_INDEX_TYPE:
                        model.setType((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_TIMING:
                        model.setTiming((Integer) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_SIGN_ID:
                        model.setSignID((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_ADDRESS:
                        model.setAddress((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_SUB_ITEM:
                        model.setSubItem((String) getCellValue(cell));
                        break;
                    default:
                        break;
                }

            }
            ModelTransmitter.add(model);
        }

        return ModelTransmitter;
    }

    // get controller export
    private static List<ModelItem> getOutPuts(String file) throws IOException {
        List<ModelItem> modelItems = new ArrayList<>();
        Scanner scanner = new Scanner(new File(PATH + "controller/" + file + ".csv"));
        while (scanner.hasNextLine()) {
            String iTem = getRecordFromLine(scanner.nextLine()).get(0);
            if (iTem.lastIndexOf("|") == -1)
                continue;
            String parentItem = iTem.substring(0, iTem.lastIndexOf("|"));
            String subItem = iTem.substring(iTem.lastIndexOf("|") + 1);
            modelItems.add(
                    ModelItem.builder()
                            .parentName(parentItem)
                            .subItem(subItem)
                            .build()
            );
        }
        return modelItems;
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    // Get cell value
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                double numericCellValue = cell.getNumericCellValue();
                cellValue = (int) numericCellValue;
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
