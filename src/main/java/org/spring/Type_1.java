package org.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Type_1 {
    public static void main(String[] args) {
        String input =
                "D15006.0\n" +
                        "D15006.1\n" +
                        "D15007.0\n" +
                        "D15007.1\n" +
                        "D15008.0\n" +
                        "D15008.1\n";
        List<String> dataTrain = new ArrayList<>(Arrays.asList(input.split("\n")));
        System.out.println("Input Item's Name: ");
        Scanner scanner = new Scanner(System.in);
        String itemName = scanner.next();

        int offset = 0;

        System.out.println("Item Name,IsGroupMember,Item Enabled,Variable Enabled,Variable Name,Variable Option,Variable Class,Variable Object Name,Variable Object Option,Class Hierarchy,File Object Delimiter,Description Enabled,Description,Attribute,Link Item,Initialize Enabled,Initialize Data,Initialize Type,Force to Change,Use Empty,Deactivate,Use History,OnChange Events,Deadband Enabled,Deadbnad Min Value,Deadbnad Max Value,Filter Enabled,Filter Min Value,Filter Max Value,Chattering Enabled,Chattering Max Value,Mask Enabled,Mask Value,BCD Enabled,Request Enabled,Request Type,Request Enabled (Write),Request Type (Write),Allowed Min Value,Allowed Max Value,Allowed Step Value,Allowed List,Allowed Default,Misc Data Type,BLOB Size,BLOB Text,EU Unit,Compare Arrays,Extract Linking,Item Type,Link Property,Sub Item,Sub Item Block Size,Sub Item Block Update,Sub Item Parent Name,Sub Item Offset,Sub Item Data Type,Sub Item Unit Size,Sub Item Elem Count,Sub Item Byte Order,Direct Reading,Direct Reading Suppressed Time,Sub Item Offset Unit,Sub Item Bit,Sub Item Bit Position,Parent Item Data Type,Parent Item Unit Size,Parent Item Elem Count,Not Reconnect Judge Item,Server Publishing Setting\n");

        for (int i = 0; i < dataTrain.size(); i++) {

            String itemData = dataTrain.get(i);

            String item = itemData.substring(0, itemData.lastIndexOf("."));

            int bit = Integer.parseInt(itemData.substring(itemData.lastIndexOf(".") + 1));

            System.out.println("WordItem" + itemName + "|" + item + "-" + bit + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + item + "," + offset + ",17,1,1,1,False,0,0,True," + bit + ",0,0,0,False,2\n");

            if (i + 1 < dataTrain.size()) {
                int nextBit = Integer.parseInt(dataTrain.get(i + 1).substring(dataTrain.get(i + 1).lastIndexOf(".") + 1));

                if (nextBit == 0) {
                    offset++;
                }
            }
        }
    }
}
