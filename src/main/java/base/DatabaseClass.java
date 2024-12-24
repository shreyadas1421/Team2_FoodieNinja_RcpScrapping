package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseClass {

    
    private Connection conn;
   
    public Connection connect() throws SQLException {
    	System.out.println("Inside the connection method");
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/RecipeScraping", "postgres", "postgre");
        return conn;
    }

    public void createDatabase() throws SQLException {
        Connection tempConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/RecipeScraping", "postgres", "postgre");
        Statement stmt = tempConn.createStatement();
        String dropDbSQL = "DROP DATABASE IF EXISTS " +"RecipeScraping" ;
        stmt.executeUpdate(dropDbSQL);
        String createDbSQL = "CREATE DATABASE " + "RecipeScraping";
        stmt.executeUpdate(createDbSQL);
        stmt.close();
        tempConn.close();
    }
/*******************     Create table   *************************************************************/
    public void createTableAllRecipies() throws SQLException {
        if (conn == null || conn.isClosed()) {
            connect();
        }
        Statement stmt = conn.createStatement();
        
        String createTableSQL = "CREATE TABLE IF NOT EXISTS AllRecipes (" +
                "recipe_id VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "food_category VARCHAR(255)," +
                "ingredients TEXT," +
                "prep_time VARCHAR(255)," +
                "cook_time VARCHAR(255)," +
                "No_of_servings VARCHAR(255)," +
                "Recipe_Descriptions TEXT," +
                "Preparation_method TEXT," +
                "Nutrient_values TEXT," +
                "Recipe_URL VARCHAR(255)" +
                ")";
        stmt.executeUpdate(createTableSQL);
        String deleteTableSQL ="Delete from AllRecipes";
        stmt.execute(deleteTableSQL);       
        stmt.close();
        System.out.println("Inside the create Query");
    }
    
    public void createTableLCHFEliminate() throws SQLException {
        if (conn == null || conn.isClosed()) {
            connect();
        }
        Statement stmt = conn.createStatement();
        
        String createTableSQL = "CREATE TABLE IF NOT EXISTS AllRecipes (" +
                "recipe_id VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "food_category VARCHAR(255)," +
                "ingredients TEXT," +
                "prep_time VARCHAR(255)," +
                "cook_time VARCHAR(255)," +
                "No_of_servings VARCHAR(255)," +
                "Recipe_Descriptions TEXT," +
                "Preparation_method TEXT," +
                "Nutrient_values TEXT," +
                "Recipe_URL VARCHAR(255)" +
                ")";
        stmt.executeUpdate(createTableSQL);
        String deleteTableSQL ="Delete from AllRecipes";
        stmt.execute(deleteTableSQL);       
        stmt.close();
        System.out.println("Inside the create Query");
    }
    
    public void createTableLFVEliminate() throws SQLException {
        if (conn == null || conn.isClosed()) {
            connect();
        }
        Statement stmt = conn.createStatement();
        
        String createTableSQL = "CREATE TABLE IF NOT EXISTS LFVEliminate (" +
                "recipe_id VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "food_category VARCHAR(255)," +
                "ingredients TEXT," +
                "prep_time VARCHAR(255)," +
                "cook_time VARCHAR(255)," +
                "No_of_servings VARCHAR(255)," +
                "Recipe_Descriptions TEXT," +
                "Preparation_method TEXT," +
                "Nutrient_values TEXT," +
                "Recipe_URL VARCHAR(255)" +
                ")";
        stmt.executeUpdate(createTableSQL);
        String deleteTableSQL ="Delete from LFVEliminate";
        stmt.execute(deleteTableSQL);       
        stmt.close();
        System.out.println("Inside the LFVEliminate Query");
    }
    
    /*******************     Insert table   *************************************************************/   

    public void insertRecipeDataLFVEliminate(String recipeId, String name, String foodcategory,String ingredients,String preptime,
    		                     String cookTime,String servings, String recipedescription, String prepmethod, String nutrientvalues, String rpvalue) throws SQLException {
        if (conn == null || conn.isClosed()) {
            connect();
        }
        String insertSQL = "INSERT INTO LFVEliminate (recipe_id, name,food_category,ingredients, prep_time, cook_time,No_of_servings,Recipe_Descriptions,Preparation_method,Nutrient_values,Recipe_URL) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
        preparedStatement.setString(1, recipeId);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, foodcategory);
        preparedStatement.setString(4, ingredients);
        preparedStatement.setString(5, preptime);
        preparedStatement.setString(6, cookTime);
        preparedStatement.setString(7, servings);
        preparedStatement.setString(8, recipedescription);
        preparedStatement.setString(9, prepmethod);
        preparedStatement.setString(10, nutrientvalues);
        preparedStatement.setString(11, rpvalue);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        
        System.out.println("Inside the LFVEliminate  insert Query");
    }
    
    public void insertRecipeDataAllRecipies(String recipeId, String name, String foodcategory,String ingredients,String preptime,
            String cookTime,String servings, String recipedescription, String prepmethod, String nutrientvalues, String rpvalue) throws SQLException {
		if (conn == null || conn.isClosed()) {
		connect();
		}
		String insertSQL = "INSERT INTO AllRecipes (recipe_id, name,food_category,ingredients, prep_time, cook_time,No_of_servings,Recipe_Descriptions,Preparation_method,Nutrient_values,Recipe_URL) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
		preparedStatement.setString(1, recipeId);
		preparedStatement.setString(2, name);
		preparedStatement.setString(3, foodcategory);
		preparedStatement.setString(4, ingredients);
		preparedStatement.setString(5, preptime);
		preparedStatement.setString(6, cookTime);
		preparedStatement.setString(7, servings);
		preparedStatement.setString(8, recipedescription);
		preparedStatement.setString(9, prepmethod);
		preparedStatement.setString(10, nutrientvalues);
		preparedStatement.setString(11, rpvalue);
		preparedStatement.executeUpdate();
		preparedStatement.close();
		
		System.out.println("Inside the insert Query");
}
    

    public void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}