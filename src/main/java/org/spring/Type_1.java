package org.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Type_1 {
    public static void main(String[] args) {
        String input =
                "E4010.0\n" +
                        "E4010.1\n" +
                        "E4010.2\n" +
                        "E4010.3\n" +
                        "E4010.4\n" +
                        "E4010.5\n" +
                        "E4010.6\n" +
                        "E4010.7\n" +
                        "E4011.0\n" +
                        "E4011.1\n" +
                        "E4011.2\n" +
                        "E4011.3\n" +
                        "E4011.4\n" +
                        "E4011.5\n" +
                        "E4011.6\n" +
                        "E4011.7\n" +
                        "E4012.0\n" +
                        "E4012.1\n" +
                        "E4012.2\n";
        List<String> dataTrain = new ArrayList<>(Arrays.asList(input.split("\n")));

        String parentItem = dataTrain.get(0).substring(0, dataTrain.get(0).lastIndexOf("."));
        System.out.println("Item's Name: " + parentItem);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Data Type (UI1: 17; UI2: 18)");
        int dataType = scanner.nextInt();

        int offset = 0;

        System.out.println("Item Name,IsGroupMember,Item Enabled,Variable Enabled,Variable Name,Variable Option,Variable Class,Variable Object Name,Variable Object Option,Class Hierarchy,File Object Delimiter,Description Enabled,Description,Attribute,Link Item,Initialize Enabled,Initialize Data,Initialize Type,Force to Change,Use Empty,Deactivate,Use History,OnChange Events,Deadband Enabled,Deadbnad Min Value,Deadbnad Max Value,Filter Enabled,Filter Min Value,Filter Max Value,Chattering Enabled,Chattering Max Value,Mask Enabled,Mask Value,BCD Enabled,Request Enabled,Request Type,Request Enabled (Write),Request Type (Write),Allowed Min Value,Allowed Max Value,Allowed Step Value,Allowed List,Allowed Default,Misc Data Type,BLOB Size,BLOB Text,EU Unit,Compare Arrays,Extract Linking,Item Type,Link Property,Sub Item,Sub Item Block Size,Sub Item Block Update,Sub Item Parent Name,Sub Item Offset,Sub Item Data Type,Sub Item Unit Size,Sub Item Elem Count,Sub Item Byte Order,Direct Reading,Direct Reading Suppressed Time,Sub Item Offset Unit,Sub Item Bit,Sub Item Bit Position,Parent Item Data Type,Parent Item Unit Size,Parent Item Elem Count,Not Reconnect Judge Item,Server Publishing Setting\n");

        for (int i = 0; i < dataTrain.size(); i++) {

            String item = dataTrain.get(i);

            String itemAddress = item.substring(0, item.lastIndexOf("."));

            String bitCharacter = item.substring(item.lastIndexOf(".") + 1);

            int bit = getBit(bitCharacter) == null ? Integer.parseInt(bitCharacter) : getBit(bitCharacter);

            System.out.println("WordItem" + parentItem + "|" + itemAddress + "-" + bit + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + itemAddress + "," + offset + "," + dataType + ",1,1,1,False,0,0,True," + bit + ",0,0,0,False,2\n");

            if (i + 1 < dataTrain.size()) {
                String nextBitCharacter = dataTrain.get(i + 1).substring(dataTrain.get(i + 1).lastIndexOf(".") + 1);

                int nextBit = getBit(nextBitCharacter) == null ? Integer.parseInt(nextBitCharacter) : getBit(nextBitCharacter);

                if (nextBit == 0) {
                    offset++;
                }
            }
        }
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
}
