package LibraryManagement.Main.Classes

import java.sql.DriverManager.getConnection
import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet, SQLException, Statement}


case class Category(categoryId: String, categoryName: String)
 {

  // CATEGORY SECTION
  def addCategory(categoryId: String, categoryName: String): Unit = {
    val sql = "INSERT INTO category (id, category_name) VALUES (?, ?)"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)


    statement.setString(1, categoryId)
    statement.setString(2, categoryName)

    try {
      statement.executeUpdate()
      println("Category added successfully!")
    } catch {
      case e: SQLException =>
        println("Error occurred while adding the category: " + e.getMessage)
    } finally {
      statement.close()
      connection.close()
    }
  }

  def editCategory(categoryId: String, newCategoryName: String): Unit = {
    val sql = "UPDATE category SET category_name = ? WHERE id = ?"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)

    val statement: PreparedStatement = connection.prepareStatement(sql)

    statement.setString(1, newCategoryName)
    statement.setString(2, categoryId)

    try {
      val rowsAffected = statement.executeUpdate()
      if (rowsAffected > 0) {
        println("Category updated successfully!")
      } else {
        println("Category not found.")
      }
    } catch {
      case e: SQLException =>
        println("Error occurred while editing the category: " + e.getMessage)
    } finally {
      statement.close()
      connection.close()
    }
  }

  def deleteCategory(categoryId: String): Unit = {
    val sql = "DELETE FROM category WHERE id = ?"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)

    statement.setString(1, categoryId)

    try {
      val rowsAffected = statement.executeUpdate()
      if (rowsAffected > 0) {
        println("Category deleted successfully!")
      } else {
        println("Category not found.")
      }
    } catch {
      case e: SQLException =>
        println("Error occurred while deleting the category: " + e.getMessage)
    } finally {
      statement.close()
      connection.close()
    }
  }

  def viewAllCategories(): Unit = {
    val sql = "SELECT * FROM category"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)
    val resultSet: ResultSet = statement.executeQuery(sql)

    println("All Categories:")
    println("=================")
    while (resultSet.next()) {
      val categoryId = resultSet.getString("id")
      val categoryName = resultSet.getString("name")
      println(s"Category ID: $categoryId, Name: $categoryName")
    }
    println("=================")

    resultSet.close()
    statement.close()
    connection.close()
  }

  def getCategoryById(categoryId: String): Unit = {
    val sql = "SELECT * FROM category WHERE category_id = ?"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, categoryId)
    val resultSet: ResultSet = statement.executeQuery()

    if (resultSet.next()) {
      val id = resultSet.getString("category_id")
      val name = resultSet.getString("name")
      Some(Category(id, name))
    } else {
      None
    }

    resultSet.close()
    statement.close()
    connection.close()
  }


}

