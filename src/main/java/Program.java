import org.w3c.dom.*;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;

public class Program {
    private String url;
    private String username;
    private String password;
    private Integer N;

    public void setURL(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setN(Integer N) {
        this.N = N;
    }

    public String getUrl(){
        return this.url;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public int getN() {
        return this.N;
    }

    public void insertIntoTest(){
        try {
            String query = "select * from test";
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
                Statement stat = conn.createStatement();
                ResultSet rs = stat.executeQuery(query);
                if (rs.next()){
                    stat.executeUpdate("DELETE FROM test");
                }
                for (int i = 1; i <= getN(); i++) {
                    stat.executeUpdate("INSERT INTO test SELECT " + i);
                }
            }
        } catch (Exception exc) {
            System.out.println("Connection failed...");
        }
    }

    public ArrayList<Integer> getFromTest(){
        try {
            String query = "select * from test";
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
                Statement stat = conn.createStatement();
                ResultSet rs = stat.executeQuery(query);
                ArrayList<Integer> result = new ArrayList<Integer>();
                while (rs.next()){
                    result.add(rs.getInt(1));
                }
                return result;
            }
        } catch (Exception exc) {
            System.out.println("Connection failed...");
            return null;
        }
    }

    private static Node createEntryElement(Document doc, Element element, String name, int value){
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(Integer.toString(value)));
        return node;
    }

    private static Node createEntry(Document doc, int N){
        Element entry = doc.createElement("entry");
        entry.appendChild(createEntryElement(doc, entry, "field", N));
        return entry;
    }

    public void createXML(ArrayList<Integer> rs) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("entries");
            doc.appendChild(rootElement);
            for (int i = 0; i < rs.size(); i++){
                rootElement.appendChild(createEntry(doc, rs.get(i)));
            }


            DOMSource source = new DOMSource(doc);

            File filename = new File("1.xml");
            if (!filename.createNewFile()){
                filename.delete();
                filename.createNewFile();
            }

            StreamResult file = new StreamResult(filename);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, file);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void convertXMLOverXSLT() throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("C://Users//user//IdeaProjects//test_junior//xslt.xsl"));
        Transformer transformer = factory.newTransformer(xslt);
        Source xml = new StreamSource(new File("C://Users//user//IdeaProjects//test_junior//1.xml"));
        transformer.transform(xml, new StreamResult(new File("C://Users//user//IdeaProjects//test_junior//2.xml")));
    }

    public Integer getArithmeticSum() {
        try {
            int sum = 0;
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File inputFile = new File("C://Users//user//IdeaProjects//test_junior//2.xml");
            Document document = documentBuilder.parse(inputFile);
            NodeList nList = document.getElementsByTagName("entry");
            for (int i = 0; i < nList.getLength(); ++i){
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    sum += Integer.parseInt(eElement.getAttribute("field"));
                }
            }
            return sum;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

