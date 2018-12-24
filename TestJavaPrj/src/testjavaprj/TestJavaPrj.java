package testjavaprj;

import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Scanner;

public class TestJavaPrj {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";

    static final String USER = "sa";
    static final String PASS = "";

    static Connection conn;
    static Statement stmt;

    public static void main(String[] args) {

        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");

            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
        while(true) {
            int choice = menu();
            switch (choice) {
                case 1:
                    addField();
                    break;
                case 2:
                    deleteField();
                    break;
                case 3:
                    showFields();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Такого пункта нет, повторите ввод");
            }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int menu() {
        int choice = -1;
        Scanner input = new Scanner(System.in);

        System.out.println("Меню:");
        System.out.println("1 - Добавить расход");
        System.out.println("2 - Удалить расход");
        System.out.println("3 - Вывод расходов");
        System.out.println("0 - Выход");

        try{
            choice =  input.nextInt();
        } catch(Exception ex){
            System.out.println("Вы ввели некорректное значение");
        }
        return choice;
    }

    public static void addField() throws SQLException{
        Scanner input = new Scanner(System.in);
        System.out.println("Выберите группу расхода:");
        int choice = showGroups();
        System.out.println("Введите сумму расхода: ");
        int sum = input.nextInt();
        System.out.println("Введите описание: ");
        String descript = input.nextLine();

        String query = "INSERT INTO budget (budget_type_id_fk, description,account_id_fk, charge_value) VALUES (?, ?, 1, ?)";

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, choice);
        preparedStatement.setString(2, descript);
        preparedStatement.setInt(3, sum);

        preparedStatement.execute();

        /*
        try{
            int price = input.nextInt();
            SomeField obj = new SomeField(name, price);
            SomeFieldsMap.addField(obj);
        }catch(Exception ex){
            System.out.println("Вы ввели некорректную цену");
        }*/
    }

    public static void deleteField(){
        Scanner input = new Scanner(System.in);
        System.out.println("Введите id объекта, который необходимо удалить: ");
        int id = input.nextInt();
        while(id<=0 || id>(SomeFieldsMap.getSizeMap()+1) || !SomeFieldsMap.isElementEmpty(id)){
            if(!SomeFieldsMap.isMapEmpty()){
                System.out.println("Неверный индекс! Выход за границы диапазона\nПопробуйте заново");
                id = input.nextInt();
            } else {
                System.out.println("У Вас нет значений. Добавьте новые (1) или завершите работу (0)");
                return;
            }
            }
        SomeFieldsMap.deleltField(id);

    }

    public static void showFields() throws SQLException{
        System.out.println("Расходы за месяц по группам:");
        ResultSet res = stmt.executeQuery(
                "WITH RECURSIVE Report11 (budget_type_id, name, required, summa) AS\n" +
                "(\n" +
                "    SELECT b_type.budget_type_id, b_type.name, b_type.required, sum(b.charge_value)\n" +
                "    FROM budget_type b_type, budget b\n" +
                "    WHERE b_type.budget_type_id = 1\n" +
                "                   AND b.operation_type = false\n" +
                "                   AND b.budget_type_id_fk IN (SELECT budget_type_id FROM budget_type WHERE group_id = 1\n" +
                "                                                                   UNION ALL\n" +
                "                                                                   SELECT budget_type_id FROM budget_type WHERE budget_type_id = 1)\n" +
                "     GROUP BY b_type.budget_type_id\n" +
                "           UNION ALL\n" +
                "    SELECT nested_budgT.budget_type_id, nested_budgT.name, nested_budgT.required, sum(nested_budg.charge_value)\n" +
                "    FROM Report11 rep\n" +
                "    INNER JOIN budget_type nested_budgT, budget nested_budg\n" +
                "    WHERE nested_budgT.group_id = rep.budget_type_id\n" +
                "                   AND nested_budgT.budget_type_id = nested_budg.budget_type_id_fk\n" +
                "                   AND nested_budg.operation_type = false\n" +
                "    GROUP BY(nested_budgT.budget_type_id)\n" +
                "),\n" +
                "Report12 (budget_type_id, name, required, summa) AS\n" +
                "(\n" +
                "    SELECT b_type.budget_type_id, b_type.name, b_type.required, sum(b.charge_value)\n" +
                "    FROM budget_type b_type, budget b\n" +
                "    WHERE budget_type_id = 2\n" +
                "                   AND b.operation_type = false\n" +
                "                   AND b.budget_type_id_fk IN (SELECT budget_type_id FROM budget_type WHERE group_id = 2\n" +
                "                                                                   UNION ALL\n" +
                "                                                                   SELECT budget_type_id FROM budget_type WHERE budget_type_id = 2)\n" +
                "    GROUP BY b_type.budget_type_id\n" +
                "        UNION ALL\n" +
                "    SELECT nested_budgT.budget_type_id, nested_budgT.name, nested_budgT.required, sum(nested_budg.charge_value)\n" +
                "    FROM Report12 rep\n" +
                "    INNER JOIN budget_type nested_budgT, budget nested_budg\n" +
                "    WHERE nested_budgT.group_id = rep.budget_type_id\n" +
                "                   AND nested_budgT.budget_type_id = nested_budg.budget_type_id_fk\n" +
                "                   AND nested_budg.operation_type = false\n" +
                "    GROUP BY(nested_budgT.budget_type_id)\n" +
                "),\n" +
                "Report13 (budget_type_id, name, required, summa) AS\n" +
                "(\n" +
                "    SELECT b_type.budget_type_id, b_type.name, b_type.required, sum(b.charge_value)\n" +
                "    FROM budget_type b_type, budget b\n" +
                "    WHERE budget_type_id = 3\n" +
                "                   AND b.operation_type = false\n" +
                "                   AND b.budget_type_id_fk IN (SELECT budget_type_id FROM budget_type WHERE group_id = 3\n" +
                "                                                                   UNION ALL\n" +
                "                                                                   SELECT budget_type_id FROM budget_type WHERE budget_type_id = 3)\n" +
                "    GROUP BY b_type.budget_type_id\n" +
                "        UNION ALL\n" +
                "    SELECT nested_budgT.budget_type_id, nested_budgT.name, nested_budgT.required, sum(nested_budg.charge_value)\n" +
                "    FROM Report13 rep\n" +
                "    INNER JOIN budget_type nested_budgT, budget nested_budg\n" +
                "    WHERE nested_budgT.group_id = rep.budget_type_id\n" +
                "                   AND nested_budgT.budget_type_id = nested_budg.budget_type_id_fk\n" +
                "                   AND nested_budg.operation_type = false\n" +
                "    GROUP BY(nested_budgT.budget_type_id)\n" +
                "),\n" +
                "totalSum (tSum) AS\n" +
                "(\n" +
                "    SELECT sum(charge_value)\n" +
                "    FROM budget\n" +
                "    WHERE operation_type = false \n" +
                ")\n" +
                "\n" +
                "SELECT * FROM Report11\n" +
                "UNION ALL\n" +
                "SELECT * FROM Report12\n" +
                "UNION ALL\n" +
                "SELECT * FROM Report13\n" +
                "UNION ALL\n" +
                "SELECT NULL, 'TOTAL: ', NULL, tSum\n" +
                "FROM totalSum;"
        );

        System.out.println("ID\tНаименование\tОбязательный/необязательный\t\tСумма");

        while (res.next()){
            if (res.getString("name").length() > 10){
                System.out.println(res.getInt("budget_type_id") + "\t\t" +
                        res.getString("name").substring(0, 10) + "\t\t\t\t\t" +
                        res.getBoolean("required") + "\t\t\t" +
                        res.getInt("summa"));
            } else {
                System.out.println(res.getInt("budget_type_id") + "\t\t" +
                        res.getString("name") + "\t\t\t\t\t" +
                        res.getBoolean("required") + "\t\t\t" +
                        res.getInt("summa"));
            }
        }
        /*System.out.printf("%3s%20s%8s\n", "ID", "NAME", "PRICE");
        System.out.println("-----------------------------------------------");
        Map<Integer, SomeField> fieldList = SomeFieldsMap.getFields();
        fieldList.forEach((key, value) -> System.out.println(value.toString()));
        counter(fieldList);*/
    }

    public static int showGroups(){
        int choice = -1;
        Scanner input = new Scanner(System.in);

        System.out.println("1 - Питание");
        System.out.println("2 - Квартирные платежи");
        System.out.println("3 - Проезд");

        try{
            choice =  input.nextInt();
        } catch(Exception ex){
            System.out.println("Вы ввели некорректное значение");
        }
        return choice;
    }

    public static void counter(Map<Integer, SomeField> sf){
        int count = 0;
        for (Map.Entry<Integer, SomeField> entry : sf.entrySet()){
            count += entry.getValue().getPrice();
        }
        System.out.printf("%23s%8d\n", "COUNT:", count);
        System.out.println("Нажмите клавишу <Enter> для выхода в главное меню");
        try {
            while (System.in.read() != 10){
                System.out.println("Нажмите клавишу <Enter> для выхода в главное меню");
                System.in.read();
            }
        } catch (IOException ex) {
            System.out.println("Нажмите клавишу <Enter> для выхода в главное меню");
        }

    }

}
