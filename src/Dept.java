import java.sql.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

//MySQL's Connector/J driver is a Type 4 driver

@SuppressWarnings("java:S106")
public class Dept {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306";
    static final String DB_NAME = "test1";

    static final String USER = "root";
    static final String PASS = "dedsec";

    private static Statement statement = null;
    private static Connection connection = null;
    private static ResultSet resultSet = null;

    private static void selectDB(LoadFrame loadFrame) throws SQLException {
        boolean dbExists = false;
        resultSet = null;

        // check if db exists
        resultSet = connection.getMetaData().getCatalogs();
        while (resultSet.next()) {
            if (DB_NAME.equals(resultSet.getString(1))) {
                dbExists = true;
            }
        }

        if (dbExists) {
            loadFrame.updateProgress("Using " + DB_NAME, 50);
            System.out.println("--Database exists--");
            statement.executeUpdate("USE " + DB_NAME);
            System.out.println("--Using " + DB_NAME + "--");
        } else {
            loadFrame.updateProgress("Creating database, " + DB_NAME, 50);
            System.out.println("--Creating database, " + DB_NAME + "--");
            statement.executeUpdate("CREATE DATABASE " + DB_NAME);
            statement.executeUpdate("USE " + DB_NAME);
            System.out.println("--Using " + DB_NAME + "--");
            loadFrame.updateProgress("Updating databse", 75);
            generateTables();
            System.out.println("--Database updated--");
        }
        loadFrame.updateProgress("Done...", 100);
        resultSet.close();
    }

    //////////////////////////////// ASSUME Dnumber is primary key for DEPARTMENT
    //////////////////////////////// and foreign key for DEPT_LOCATIONS
    private static void generateTables()
            throws SQLException {
        // set auto commit false for batch processing
        connection.setAutoCommit(false);

        statement.executeUpdate("CREATE TABLE DEPARTMENT (Dname VARCHAR(32), Dnumber TINYINT NOT NULL," +
                " Mgr_ssn VARCHAR(16), Mgr_start_date DATE, PRIMARY KEY (Dnumber))");

        String insertIN = "INSERT INTO DEPARTMENT (Dname, Dnumber, Mgr_ssn, Mgr_start_date)";

        statement.addBatch(insertIN + " VALUES('Research', 5, 333445555, '1988-05-22')");
        statement.addBatch(insertIN + " VALUES('Administration', 4, 987654321, '1995-01-01')");
        statement.addBatch(insertIN + " VALUES('Headquarters', 1, 888665555, '1981-06-19')");

        statement.executeUpdate("CREATE TABLE DEPT_LOCATIONS (Dnumber TINYINT NOT NULL, Dlocation VARCHAR(32)," +
                " PRIMARY KEY (Dlocation), FOREIGN KEY (Dnumber) REFERENCES DEPARTMENT (Dnumber))");

        insertIN = "INSERT INTO DEPT_LOCATIONS (Dnumber, Dlocation)";

        statement.addBatch(insertIN + " VALUES(1, 'Houston')");
        statement.addBatch(insertIN + " VALUES(4, 'Stafford')");
        statement.addBatch(insertIN + " VALUES(5, 'Bellaire')");
        statement.addBatch(insertIN + " VALUES(5, 'Sugarland')");
        statement.addBatch(insertIN + " VALUES(5, 'Dallas')"); /////////////////////////////////////// REPLACING WITH
                                                               /////////////////////////////////////// HOUSTON

        statement.executeBatch();
        connection.commit();

        connection.setAutoCommit(true);
    }

    public static Map<String, Integer> getColInfo(String table) throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM " + table);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int col = resultSet.getMetaData().getColumnCount();

        Map<String, Integer> colType = Collections.synchronizedMap(new LinkedHashMap<String, Integer>());

        for (int i = 1; i <= col; i++) {
            colType.put(resultSetMetaData.getColumnName(i), resultSetMetaData.getColumnType(i));
        }

        return colType;
    }

    public static String[][] showTable(String table) throws SQLException {
        resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + table);
        resultSet.next();
        int row = resultSet.getInt(1);

        resultSet = statement.executeQuery("SELECT * FROM " + table);

        Map<String, Integer> colInfo = getColInfo(table);
        int[] colTypes = new int[colInfo.size()];
        String[][] tableValues = new String[row + 1][colInfo.size()];

        int i = 0;
        for (Map.Entry<String, Integer> entry : colInfo.entrySet()) {
            tableValues[0][i] = entry.getKey();
            colTypes[i++] = entry.getValue();
        }

        i = 1;
        while (resultSet.next()) {
            for (int j = 0; j < colInfo.size(); j++) {
                tableValues[i][j] = MapDType.getValue(colTypes[j], tableValues[0][j], resultSet);
            }
            i++;
        }
        return tableValues;
    }

    public static void insertValue(String table, List<JTextField> data)
            throws SQLException {

        Map<String, Integer> colInfo = getColInfo(table);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection
                    .prepareStatement("INSERT INTO " + table + " VALUES (?" + ",?".repeat(colInfo.size() - 1) + ")");

            int i = 0;
            for (Map.Entry<String, Integer> entry : colInfo.entrySet()) {
                MapDType.setValue(entry.getValue(), (i + 1), data.get(i++).getText(), preparedStatement);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public static void updateValue(String table, Map<String, String> set, Map<String, String> where)
            throws SQLException {
        PreparedStatement preparedStatement = null;

        Map<String, Integer> colInfo = getColInfo(table);

        StringBuilder setQueries = new StringBuilder("");
        StringBuilder whereQueries = new StringBuilder("");

        try {
            int i = 0;
            for (Map.Entry<String, String> entry : set.entrySet()) {
                setQueries.append((i++ != 0 ? ", " : "") + entry.getKey() + "=? ");
            }
            i = 0;
            for (Map.Entry<String, String> entry : where.entrySet()) {
                whereQueries.append((i++ != 0 ? ", " : "") + entry.getKey() + "=? ");
            }

            preparedStatement = connection.prepareStatement(
                    "UPDATE " + table + " SET " + setQueries.toString()
                            + (whereQueries.length() > 0 ? "WHERE " + whereQueries.toString() : ""));

            i = 1;
            for (Map.Entry<String, String> entry : set.entrySet()) {
                MapDType.setValue(colInfo.get(entry.getKey()), i++, entry.getValue(), preparedStatement);
            }

            for (Map.Entry<String, String> entry : where.entrySet()) {
                MapDType.setValue(colInfo.get(entry.getKey()), i++, entry.getValue(), preparedStatement);
            }

            preparedStatement.executeUpdate();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public static void deleteValue(String table, Map<String, String> where)
            throws SQLException {
        PreparedStatement preparedStatement = null;

        Map<String, Integer> colInfo = getColInfo(table);

        StringBuilder whereQueries = new StringBuilder("");

        try {
            int i = 0;
            for (Map.Entry<String, String> entry : where.entrySet()) {
                whereQueries.append((i++ != 0 ? ", " : "") + entry.getKey() + "=? ");
            }

            preparedStatement = connection.prepareStatement(
                    "DELETE FROM " + table + " WHERE " + whereQueries.toString());

            i = 1;
            for (Map.Entry<String, String> entry : where.entrySet()) {
                MapDType.setValue(colInfo.get(entry.getKey()), i++, entry.getValue(), preparedStatement);
            }

            preparedStatement.executeUpdate();

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public static void cleanUp() throws SQLException {
        // clean up env
        System.out.println("--Cleaning up--");
        resultSet.close();
        statement.close();
        connection.close();
    }

    public static void main(String[] args) throws SQLException {
        try {
            LoadFrame loadFrame = new LoadFrame();

            // load and register driver
            loadFrame.updateProgress("Registering driver", 0);
            Class.forName(JDBC_DRIVER);
            System.out.println("--Driver successfully loaded and registered--");

            // establish connection
            loadFrame.updateProgress("Establishing connection", 25);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("--Connection established--");

            // create statement obj
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // load database if exists or generate
            selectDB(loadFrame);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
