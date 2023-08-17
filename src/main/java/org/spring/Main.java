package org.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String input =
                "M10500-0\n" +
                        "M10501-1\n" +
                        "M10502-2\n" +
                        "M10503-3\n" +
                        "M10504-4\n" +
                        "M10505-5\n" +
                        "M10506-6\n" +
                        "M10507-7\n" +
                        "M10508-0\n" +
                        "M10509-1\n" +
                        "M10510-0\n" +
                        "M10511-1\n" +
                        "M10512-2\n" +
                        "M10513-3\n" +
                        "M10514-4\n" +
                        "M10515-5\n" +
                        "M10516-6\n" +
                        "M10517-7\n" +
                        "M10518-0\n" +
                        "M10519-1\n" +
                        "M10520-0\n" +
                        "M10521-1\n" +
                        "M10522-2\n" +
                        "M10523-3\n" +
                        "M10524-4\n" +
                        "M10525-5\n" +
                        "M10526-6\n" +
                        "M10527-7\n" +
                        "M10528-0\n" +
                        "M10529-1\n" +
                        "M10530-0\n" +
                        "M10531-1\n" +
                        "M10532-2\n" +
                        "M10533-3\n" +
                        "M10534-4\n" +
                        "M10535-5\n" +
                        "M10536-6\n" +
                        "M10537-7\n" +
                        "M10538-0\n" +
                        "M10539-1\n" +
                        "M10540-0\n" +
                        "M10541-1\n" +
                        "M10542-2\n" +
                        "M10543-3\n" +
                        "M10544-4\n" +
                        "M10545-5\n" +
                        "M10546-6\n" +
                        "M10547-7\n" +
                        "M10548-0\n" +
                        "M10549-1";
        List<String> dataTrain = new ArrayList<>(Arrays.asList(input.split("\n")));
        System.out.println("Input Name Item: ");
        Scanner scanner = new Scanner(System.in);
        String itemName = scanner.next();
        System.out.println("Input number Character: ");
        int numberCha = scanner.nextInt();

        int parentAddress = Integer.parseInt(itemName.substring(numberCha));
        System.out.println(dataTrain.size());

        int offset = 0;
        int numBitEqual0 = 0;

        System.out.println("Item Name,IsGroupMember,Item Enabled,Variable Enabled,Variable Name,Variable Option,Variable Class,Variable Object Name,Variable Object Option,Class Hierarchy,File Object Delimiter,Description Enabled,Description,Attribute,Link Item,Initialize Enabled,Initialize Data,Initialize Type,Force to Change,Use Empty,Deactivate,Use History,OnChange Events,Deadband Enabled,Deadbnad Min Value,Deadbnad Max Value,Filter Enabled,Filter Min Value,Filter Max Value,Chattering Enabled,Chattering Max Value,Mask Enabled,Mask Value,BCD Enabled,Request Enabled,Request Type,Request Enabled (Write),Request Type (Write),Allowed Min Value,Allowed Max Value,Allowed Step Value,Allowed List,Allowed Default,Misc Data Type,BLOB Size,BLOB Text,EU Unit,Compare Arrays,Extract Linking,Item Type,Link Property,Sub Item,Sub Item Block Size,Sub Item Block Update,Sub Item Parent Name,Sub Item Offset,Sub Item Data Type,Sub Item Unit Size,Sub Item Elem Count,Sub Item Byte Order,Direct Reading,Direct Reading Suppressed Time,Sub Item Offset Unit,Sub Item Bit,Sub Item Bit Position,Parent Item Data Type,Parent Item Unit Size,Parent Item Elem Count,Not Reconnect Judge Item,Server Publishing Setting\n");

        for (int i = 0; i < dataTrain.size(); i++) {
            int itemAddress = parentAddress + i;
            String item = itemName.substring(0, numberCha) + itemAddress;

            int bit = Integer.parseInt(dataTrain.get(i).substring(dataTrain.get(i).length() - 1));

            System.out.println("WordItem" + itemName + "|" + item + "-" + bit + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + item + "," + offset + ",17,1,1,1,False,0,0,True," + bit + ",0,0,0,False,2\n");

            if (bit == 0) {
                if (numBitEqual0 != 0) {
                    offset++;
                }
                numBitEqual0 = 1;
            }
        }
    }
}
