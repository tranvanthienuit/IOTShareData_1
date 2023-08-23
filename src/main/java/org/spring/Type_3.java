package org.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Type_3 {
    public static void main(String[] args) {
        String input =
                "E0_30\n" +
                        "E0_31\n" +
                        "E0_32\n" +
                        "E0_33\n" +
                        "E0_34\n" +
                        "E0_35\n" +
                        "E0_36\n" +
                        "E0_37\n" +
                        "E0_38\n" +
                        "E0_39\n" +
                        "E0_40\n" +
                        "E0_41\n";
        List<String> dataTrain = new ArrayList<>(Arrays.asList(input.split("\n")));
        String parentItem = dataTrain.get(0);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Item's Name: " + parentItem);

        System.out.println("Item Name,IsGroupMember,Item Enabled,Variable Enabled,Variable Name,Variable Option,Variable Class,Variable Object Name,Variable Object Option,Class Hierarchy,File Object Delimiter,Description Enabled,Description,Attribute,Link Item,Initialize Enabled,Initialize Data,Initialize Type,Force to Change,Use Empty,Deactivate,Use History,OnChange Events,Deadband Enabled,Deadbnad Min Value,Deadbnad Max Value,Filter Enabled,Filter Min Value,Filter Max Value,Chattering Enabled,Chattering Max Value,Mask Enabled,Mask Value,BCD Enabled,Request Enabled,Request Type,Request Enabled (Write),Request Type (Write),Allowed Min Value,Allowed Max Value,Allowed Step Value,Allowed List,Allowed Default,Misc Data Type,BLOB Size,BLOB Text,EU Unit,Compare Arrays,Extract Linking,Item Type,Link Property,Sub Item,Sub Item Block Size,Sub Item Block Update,Sub Item Parent Name,Sub Item Offset,Sub Item Data Type,Sub Item Unit Size,Sub Item Elem Count,Sub Item Byte Order,Direct Reading,Direct Reading Suppressed Time,Sub Item Offset Unit,Sub Item Bit,Sub Item Bit Position,Parent Item Data Type,Parent Item Unit Size,Parent Item Elem Count,Not Reconnect Judge Item,Server Publishing Setting\n");

        for (String itemAddress : dataTrain) {
            System.out.println("WordItem" + parentItem + "|" + itemAddress + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + itemAddress + ",0,17,1,1,1,False,0,0,False,0,0,0,0,False,2\n");
        }
    }
}