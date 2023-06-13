package LibraryManagement.Main.Classes

import java.sql.{Connection, Date, DriverManager, PreparedStatement, ResultSet, SQLException, Statement}
import java.sql.DriverManager.getConnection
import scala.collection.mutable.ListBuffer

case class IssueBook(issueId: Int, bookId: String, issueDate: Date, returnDate: Option[Date]) {
  // ISSUE BOOK SECTION


  def addIssueBook(bookId: String, issueId: Int, issueDate: Date, returnDate: Option[Date]): Unit = {
    val sql = "INSERT INTO issue_book (book_id, issue_id, issue_date, return_date) VALUES (?, ?, ?, ?)"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)

    statement.setString(1, bookId)
    statement.setInt(2, issueId)
    statement.setDate(3, issueDate)

    returnDate match {
      case Some(date) => statement.setDate(4, date)
      case None => statement.setNull(4, java.sql.Types.DATE)
    }

    statement.executeUpdate()
    statement.close()
    connection.close()
  }


  def editIssueBook(issueId: Int, newBookId: String, returnDate: Option[Date], newIssueDate: Date): Unit = {
    val sql = "UPDATE issue_book SET book_id = ?, student_id = ?, issue_date = ? WHERE issue_id = ?"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, newBookId)

    returnDate match {
      case Some(date) => statement.setDate(2, new java.sql.Date(date.getTime))
      case None => statement.setNull(2, java.sql.Types.DATE)
    }

    statement.setDate(3, new java.sql.Date(newIssueDate.getTime))
    statement.setInt(4, issueId)
    statement.executeUpdate()

    statement.close()
    connection.close()
  }


  def deleteIssueBook(issueId: Int): Unit = {
    val sql = "DELETE FROM issue_book WHERE issue_id = ?"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, issueId)
    statement.executeUpdate()

    statement.close()
    connection.close()
  }

  def viewAllIssueBook(): List[IssueBook] = {
    val sql = "SELECT * FROM issue_book"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)
    val resultSet: ResultSet = statement.executeQuery(sql)

    val issueBooks: ListBuffer[IssueBook] = ListBuffer.empty[IssueBook]
    while (resultSet.next()) {
      val issueId = resultSet.getInt("issue_id")
      val bookId = resultSet.getString("book_id")
      val returnDate = Option(resultSet.getObject("return_date")).map(_.asInstanceOf[Date])
      val issueDate = resultSet.getDate("issue_date")

      val issueBook = IssueBook(issueId, bookId, issueDate, returnDate)
      issueBooks += issueBook
    }

    resultSet.close()
    statement.close()
    connection.close()

    issueBooks.toList
  }


  def getIssueBookById(issueId: Int): Unit = {
    val sql = "SELECT * FROM issue_book WHERE issue_id = ?"
    // JDBC connection details
    val url = "jdbc:mysql://localhost:3306/database_sunway" // Replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // Replace with your MySQL username
    val password = "Remember121314$" // Replace with your MySQL password

    // Establish the database connection
    val connection: Connection = DriverManager.getConnection(url, username, password)
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, issueId)
    val resultSet: ResultSet = statement.executeQuery()

    if (resultSet.next()) {
      val bookId = resultSet.getString("book_id")
      val returnDate = Option(resultSet.getObject("return_date")).map(_.asInstanceOf[Date])
      val issueDate = resultSet.getDate("issue_date")

      val issueBook = IssueBook(issueId, bookId, issueDate, returnDate)
      Some(issueBook)
    } else {
      None
    }

    resultSet.close()
    statement.close()
    connection.close()
  }
}

