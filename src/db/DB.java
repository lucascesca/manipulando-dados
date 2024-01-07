package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }

        return conn;
    }
    public static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void printDataRetrieved() throws SQLException {
        PreparedStatement selectStat = getConnection().prepareStatement("SELECT " +
                        "seller.Id, " +
                        "seller.Name AS \"Nome do Vendedor\", " +
                        "Email, BirthDate, BaseSalary, " +
                        "department.Name AS \"Nome do Departamento\" " +
                        "FROM seller, department WHERE seller.DepartmentId=department.Id " +
                        "ORDER BY seller.Id",
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = selectStat.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getInt("Id") + " - " +
                    rs.getString("Nome do Vendedor") +
                    " from " + rs.getString("Nome do Departamento") +
                    " earns $" + String.format("%.2f", rs.getDouble("BaseSalary")));
        }
        closeResultSet(rs);
        closeStatement(selectStat);
    }
}
