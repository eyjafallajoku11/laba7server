package utility;

import gorod.City;

import java.sql.*;
import java.util.Objects;


public class SQLmanager {
    private static final String Template = "INSERT INTO CITIES (id, name, x, y," +
            " metersabovesealevel, creationDate, population, climate, government, " +
            "humanname, humanbirthday, standartofliving, area) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static Connection connection;
    public static void connect(){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:6969/artem","artem", "123");
            connection.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
    public static void insert(City city){
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(Template);
            stmt.setLong(1, city.getId());
            stmt.setString(2,city.getName());
            stmt.setDouble(3, city.getCoordinates().getX());
            stmt.setDouble(4, city.getCoordinates().getY());
            stmt.setDouble(5, city.getMetersAboveSeaLevel());
            stmt.setDate(6, java.sql.Date.valueOf(city.getCreationDate()));
            stmt.setLong(7, city.getPopulation());
            try {
                stmt.setString(8,city.getClimate().toString());
            } catch (NullPointerException e){
                stmt.setString(8,null);
            }
            stmt.setString(9, city.getGovernment().toString());
            try{
                stmt.setString(10, city.getGovernor().getName());
            } catch (NullPointerException e){
                stmt.setString(10,null);
            }
            try{
                stmt.setString(11, city.getGovernor().getBirthday().toString());
            } catch (NullPointerException e){
                stmt.setString(11,null);
            }
            try{
                stmt.setString(12, city.getStandardOfLiving().toString());
            } catch (NullPointerException e){
                stmt.setString(12,null);
            }
            stmt.setDouble(13, city.getArea());
        stmt.executeUpdate();
        stmt.close();
        connection.commit();
//        System.out.println("комит");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void remove(Long id){
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = "DELETE from COMPANY where ID = "+id+";";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getCollection(){
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY;");
            CityCreator creator = new CityCreator(null);
            while (rs.next()) {
                String id = Long.toString(rs.getLong("id"));
                creator.set_data(0,id);
                creator.set_data(1, String.valueOf(rs.getDouble("x")));
                creator.set_data(2, String.valueOf(rs.getDouble("y")));
                creator.set_data(3, String.valueOf(rs.getDate("creationdate")));
                creator.set_data(4, String.valueOf(rs.getDouble("area")));
                creator.set_data(5, String.valueOf(rs.getInt("population")));
                creator.set_data(6, String.valueOf(rs.getDouble("metersabovesealevel")));
                creator.set_data(7, rs.getString("climate"));
                creator.set_data(8, rs.getString("government"));
                creator.set_data(9, rs.getString("standartofliving"));
                creator.set_data(10, rs.getString("humanname"));
                if (creator.get_data(10).equals("")) {
                    creator.set_data(13, "0");
                    creator.set_data(11, "");
                } else {
                    creator.set_data(13, "1");
                    creator.set_data(11, String.valueOf(rs.getDate("humanname")));
                }
                creator.set_data(12, id);

                for (short i=0;i<14;i++){
                    if (creator.get_data(i).equals("")){
                        creator.set_data(i, null);
                    }

                }
                creator.create_city();
                creator.add_city_to_map();
                creator.clear_data();
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
