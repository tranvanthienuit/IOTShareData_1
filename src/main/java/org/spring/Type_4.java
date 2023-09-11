package org.spring;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Type_4 {
    public static void main(String[] args) {
        String input =
                "M172\n" +
                        "M72\n" +
                        "M8603\n" +
                        "M77\n" +
                        "M8604\n" +
                        "M8605\n" +
                        "D6036\n" +
                        "M81\n" +
                        "F1\n" +
                        "F2\n" +
                        "F3\n" +
                        "F4\n" +
                        "F5\n" +
                        "F6\n" +
                        "F7\n" +
                        "F8\n" +
                        "F9\n" +
                        "F10\n" +
                        "F11\n" +
                        "F12\n" +
                        "F13\n" +
                        "F14\n" +
                        "F15\n" +
                        "F16\n" +
                        "F17\n" +
                        "F20\n" +
                        "F21\n";


        List<String> data = new ArrayList<>(Arrays.asList(input.split("\n")));
        Collections.sort(data, Collator.getInstance());
        List<String> dataTrain = sort(data);

        System.out.println("Item Name,IsGroupMember,Item Enabled,Variable Enabled,Variable Name,Variable Option,Variable Class,Variable Object Name,Variable Object Option,Class Hierarchy,File Object Delimiter,Description Enabled,Description,Attribute,Link Item,Initialize Enabled,Initialize Data,Initialize Type,Force to Change,Use Empty,Deactivate,Use History,OnChange Events,Deadband Enabled,Deadbnad Min Value,Deadbnad Max Value,Filter Enabled,Filter Min Value,Filter Max Value,Chattering Enabled,Chattering Max Value,Mask Enabled,Mask Value,BCD Enabled,Request Enabled,Request Type,Request Enabled (Write),Request Type (Write),Allowed Min Value,Allowed Max Value,Allowed Step Value,Allowed List,Allowed Default,Misc Data Type,BLOB Size,BLOB Text,EU Unit,Compare Arrays,Extract Linking,Item Type,Link Property,Sub Item,Sub Item Block Size,Sub Item Block Update,Sub Item Parent Name,Sub Item Offset,Sub Item Data Type,Sub Item Unit Size,Sub Item Elem Count,Sub Item Byte Order,Direct Reading,Direct Reading Suppressed Time,Sub Item Offset Unit,Sub Item Bit,Sub Item Bit Position,Parent Item Data Type,Parent Item Unit Size,Parent Item Elem Count,Not Reconnect Judge Item,Server Publishing Setting");

        String parentItem = dataTrain.get(0);
        int offset = 0;

        for (int i = 0; i < dataTrain.size(); i++) {

            int address = Integer.parseInt(dataTrain.get(i).replaceAll("\\D+", ""));
            if (i + 1 < dataTrain.size()) {
                int nextAddress = Integer.parseInt(dataTrain.get(i + 1).replaceAll("\\D+", ""));
                if (address + 1 != nextAddress) {
                    int count = offset / 2 + 1;
                    System.out.println("WordItem" + parentItem + ",False,True,True," + parentItem + ",'ELEM=" + count + ",VT=Bit',0,,,False,\\,True,,1,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,False," + count + ",0,,0,0,0,0,0,False,0,0,False,0,8209,1," + count + ",False,2");
                    System.out.println("WordItem" + parentItem + "|" + dataTrain.get(i) + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + parentItem + "," + offset + ",17,1,1,1,False,0,0,False,0,0,0,0,False,2");
                    parentItem = dataTrain.get(i + 1);
                    offset = 0;
                } else {
                    System.out.println("WordItem" + parentItem + "|" + dataTrain.get(i) + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + parentItem + "," + offset + ",17,1,1,1,False,0,0,False,0,0,0,0,False,2");
                    offset++;
                }
            }
            if (i == dataTrain.size() - 1) {
                int count = offset / 2 + 1;
                System.out.println("WordItem" + parentItem + ",False,True,True," + parentItem + ",'ELEM=" + count + ",VT=Bit',0,,,False,\\,True,,1,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,False," + count + ",0,,0,0,0,0,0,False,0,0,False,0,8209,1," + count + ",False,2");
                System.out.println("WordItem" + parentItem + "|" + parentItem + ",False,True,False,,,0,,,False,\\,True,,0,,False,,3,0,False,False,True,False,False,0,0,False,0,0,False,0,False,0,False,False,12,False,12,,,,,,,,,,True,False,0,0,True,0,0,WordItem" + parentItem + ",0,17,1,1,1,False,0,0,False,0,0,0,0,False,2");
            }
        }
    }

    private static List<String> sort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {

                int number1 = Integer.parseInt(s1.replaceAll("\\D+", ""));
                int number2 = Integer.parseInt(s2.replaceAll("\\D+", ""));

                return number1 - number2;
            }
        });

        return list;
    }
}