import javax.xml.transform.TransformerException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test {
    public static void main(String args[]) {

        Program p = new Program();

        p.setURL("jdbc:mysql://localhost:3306/randomDB?serverTimezone=Europe/Moscow&useSSL=false");
        p.setUsername("root");
        p.setPassword("junior");
        p.setN(100);git
        p.insertIntoTest();
        p.createXML(p.getFromTest());
        try {
            p.convertXMLOverXSLT();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        System.out.println(p.getArithmeticSum());
    }
}