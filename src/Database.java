import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Martin on 30.07.2015.
 * This class connects to the Database and offers
 * an interface to execute queries and get results
 */
public class Database {
    private Connection conn;
    private boolean connection;
    private Statement statement;
    ResultSet result;

    public Database(){
        connectDatabase();
    }

    /**
     * commit() for Inserting Data into Database.
     *
     * Used for creating new Courses and new Questions
     */
    public void commit(String query){
        System.out.println("Trying to execute query: " + query);
    }


    public ArrayList<String> getCourses() throws SQLException {
        String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='quiz' ";
        ArrayList<String> results = new ArrayList<String>();
        statement = conn.createStatement();
        result = statement.executeQuery(query);
        int i = 0;
        while(result.next()) {
            results.add(result.getString("TABLE_NAME"));
            i++;
        }
        return results;
    }

    /**
     * Mapping every answer to a question
     * @param table
     * @return
     * @throws SQLException
     */
    public HashMap<String, String> getResults(String table) throws SQLException {
        HashMap<String, String> results = new HashMap<String,String>();
        String query ="SELECT question, answer FROM " + table +"";
        statement = conn.createStatement();
        result = statement.executeQuery(query);
        while(result.next()){
            results.put(result.getString("answer"),result.getString("question"));
        }
        return results;
    }

    public boolean removeTable(String table){
        boolean success = false;
        String query ="DROP TABLE " + table;
        System.out.println("Trying query : " + query);
        try {statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Successfully executed query: " + query);
            success = true;

        } catch (SQLException e) {
            System.out.println("error with createCourse()");
            e.printStackTrace();
        }
        return success;
    }
    /**
     * @param courseName table name
     * @param prof column name
     * @param age  year of the class
     */
    public boolean createCourse(String courseName, String prof, int age){
        boolean success = false;
        String course= courseName.replaceAll("\\s","");
        String query ="create Table " + course +
                " (professor varchar(255)," +
                "courseYear int," +
                "question varchar(255)," +
                "answer varchar(255))";
        System.out.println("Trying query : " + query);
        try {statement = conn.createStatement();
           statement.executeUpdate(query);
                System.out.println("Successfully executed query: " + query);
                success = true;

        } catch (SQLException e) {
            System.out.println("error with createCourse()");
            e.printStackTrace();
        }
        return success;
    }

    public boolean createQuestion(String table, String question, String answer){
        boolean success = false;
        String query ="INSERT INTO " + table + "(question,answer) values("   + "'" + question + "','" + answer+"')";
        System.out.println("Trying query : " + query);
        try {statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Successfully executed query: " + query);
            success = true;

        } catch (SQLException e) {
            System.out.println("error with createCourse()");
            e.printStackTrace();
        }
        return success;
    }

    /**
     * Basic method to establish a connection to database
     */
    private void connectDatabase(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/quiz","root","");
            connection = true;
            if(conn != null)
                System.out.println("Succesfully connected");
        } catch(Exception e){
            connection = false;
            System.out.println("Error with connectDatabase()");
            e.printStackTrace();
        }
    }
}
