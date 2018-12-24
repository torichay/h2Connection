package testjavaprj;

public class SomeField {
    private static int id = 0;
    private int fieldId = 0;
    private String name = "";
    private int price = 0;

    public SomeField(){
        id++;
        fieldId = id;
    }

    public SomeField(String name, int price){
        this();
        setName(name);
        setPrice(price);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public int getPrice(){
        return price;
    }

    public int getId(){
        return fieldId;
    }

    @Override
    public String toString() {
        return String.format("%3d%20s%8d", getId(), getName(), getPrice());
    }
}

