package lkd.namsic;

import org.junit.Test;

import java.io.Serializable;

import lkd.namsic.game.enums.object.ItemList;

public class ExampleUnitTest implements Serializable {

    @Test
    public void evalTest() {
        try {
            System.out.println("test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        String input = "";

        String itemName;
        ItemList itemData;
        for(String string : input.split("\n")) {
            try {
                itemName = string.split("\"")[1];
                itemData = ItemList.nameMap.get(itemName);

                if(itemData == null) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println(string);
                continue;
            }

            System.out.println(string.replace("\"" + itemName + "\"", "ItemList." + itemData.name())
                    .replaceAll("\", \\d{1,2}\\);", "\");"));
        }
    }

    @Test
    public void recover() {
        System.out.println("test");
    }

}