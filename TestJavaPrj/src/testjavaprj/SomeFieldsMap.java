package testjavaprj;

import java.util.HashMap;
import java.util.Map;

public class SomeFieldsMap {
    private static Map<Integer, SomeField> fieldsMap = new HashMap<>();

    public static Map<Integer, SomeField> getFields(){
        return fieldsMap;
    }

    public static void deleltField(int id){
        fieldsMap.remove(id);
    }

    public static void addField(SomeField obj){
        fieldsMap.put(obj.getId(), obj);
    }

    public static int getSizeMap(){
        return fieldsMap.size();
    }

    public static boolean isElementEmpty(int id){
        SomeField val = fieldsMap.get(id);
        if(fieldsMap.containsKey(id) && val.getName()!=""){
            System.out.println("Yes");
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMapEmpty(){
        if(fieldsMap.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }
}
