package LibraryManagement.Main.Database

import LibraryManagement.Main.Classes
import LibraryManagement.Main.Classes.{Admin, Book, BookStatus, Category, Feedback, IssueBook, Student}

import java.sql._

import scala.collection.mutable.ListBuffer
import scala.io.StdIn


class DatabaseManager {


  // import java.sql.{Connection, DriverManager}
  // establish connection using this method
  def getConnection: Connection = {
    val url = "jdbc:mysql://localhost:3306/database_sunway" // replace mydatabase with your database name
    val username = "AEDAMJUNG@GMAIL.COM" // add your MySQL username
    val password = "Remember121314$" // add your MySQL password

    // register the MySQL JDBC driver
    Class.forName("com.mysql.cj.jdbc.Driver")

    // Create the connection. DriverManage is a class that is already created in java.sql package
    DriverManager.getConnection(url, username, password)

  }


  class Login(databaseManager: DatabaseManager) {
    def login(): Unit = {
    }
  }

  class StudentLogin(databaseManager: DatabaseManager) extends Login(databaseManager) {
    override def login(): Unit = {
      println("---------------------------")
      println("Student Login")
      println("---------------------------")
      println("studentId: ")
      val studentId = StdIn.readInt()
      println("Password: ")
      val password = StdIn.readLine()
      println("---------------------------")

      try {
        // Checking with the database to see if the password and student ID matches
        val student = databaseManager.getStudentById(studentId)
        if (student.isDefined && student.get.password == password) {
          // student.isDefined check if the student is in the database.
          // student.get.password == password check if password of the student is the password in the database.
          println(s"Welcome, ${student.get.name}!") // the s allows you to enter variables in the "string"
        } else {
          println("Invalid student ID or password ")
        }
      } catch {
        case e: Exception => println(s"An error occurred while logging in ${e.getMessage}")
      }
    }


    def listStudentActions(): Unit = {
      println("=================================")
      println("+            STUDENT            +")
      println("=================================")
      println("1. Register details")
      println("2. Login to the system")
      println("3. Give feedback on books")
      println("4. Issue books")
      println("5. Change password")
      println("6. List all created books")
      println("7. Check the status of all books")
      println("8. View all books")
      println("9. Logout")
      println("=================================")

    }
  }



  class AdminLogin(databaseManager: DatabaseManager) extends Login(databaseManager) {
    override def login(): Unit = {
      println("Admin ID: ")
      val adminId = StdIn.readInt()
      println("Password: ")
      val password = StdIn.readLine()

      try {
        // Checking with the database to see if the admin ID and password match
        val adminOption = databaseManager.getAdminById(adminId)
        adminOption match {
          case Some(admin) if admin.password == password =>
            println(s"Welcome, ${admin.name}!")
          // Additional actions available for the admin can be added here
          case _ =>
            println("Invalid admin ID or password")
        }
      } catch {
        case e: Exception => println(s"An error occurred while logging in: ${e.getMessage}")
      }
    }


    def listAdminActions(): Unit = {
      println("=================================")
      println("+             ADMIN             +")
      println("=================================")
      println("1. Manage Students")
      println("2. Manage Books")
      println("3. Manage Category")
      println("4. Manage Issue Books")
      println("5. Manage Reports")
      println("=================================")
    }


    def register(databaseManager: DatabaseManager): Unit = {
      println("---------------------------")
      println("Student Registration")
      println("---------------------------")
      println("Enter your student ID:")
      val id = StdIn.readInt()
      println("Enter your name:")
      val name = StdIn.readLine()
      println("Enter your username:")
      val username = StdIn.readLine()
      println("Enter your email:")
      val email = StdIn.readLine()
      println("Enter your password:")
      val password = StdIn.readLine()
      println("---------------------------")

      try {
        // Update the student details in the database using the DatabaseManager
        val student = Student(id, name, username, email, password)
        databaseManager.insertStudent(student)
        println("New student registration created successfully!")
      } catch {
        case e: Exception =>
          println(s"An error occurred while registering student details: ${e.getMessage}")
      }
    }



  }


  def getAdminById(adminId: Int): Option[Admin] = {
    val sql = "SELECT * FROM admin WHERE adminId = ? "
    val connection: Connection = getConnection
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, adminId)

    val resultSet: ResultSet = statement.executeQuery()

    if (resultSet.next()) {
      val adminId: Int = resultSet.getInt("adminId")
      val name: String = resultSet.getString("name")
      val password: String = resultSet.getString("password")
      Some(Admin(adminId, name, password))
    } else {
      println(s"No admin found with adminId: $adminId")
      None
    }
    // Other methods
  }



  // Insert a new student into the database
  def insertStudent(student: Student): Unit = {
    val sql = "INSERT INTO students (student_id, name, username, email, password) VALUES (?, ?, ?, ?, ?)"

    //Get a connection to the database
    val connection: Connection = getConnection // get database connection

    // prepare the statement
    val statement : PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, student.studentId)
    statement.setString(2, student.name)
    statement.setString(3, student.username)
    statement.setString(4, student.email)
    statement.setString(5, student.password)

    // Execute the insert statement
    statement.executeUpdate()

    // Close the statement and connection
    statement.close()
    connection.close()

  }

  def updateStudentPassword(studentId: Int, newPassword: String): Unit = {
    val sql = "UPDATE students SET password = ? WHERE student_id = ?"

    val connection: Connection = getConnection // Get a database connection
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, newPassword)
    statement.setInt(2, studentId)

    val rowsUpdated: Int = statement.executeUpdate()

    if (rowsUpdated > 0) {
      println("Password updated successfully!")
    } else {
      println("Failed to update password.")
    }

    statement.close()
    connection.close()
  }


  // Retrieve a student by ID from the database
  def getStudentById(id: Int): Option[Student] = {
    // select all from students where the id is equal to 'condition'.
    val sql = "SELECT * FROM students WHERE id = ?"

    //get connection to the database
    val connection : Connection = getConnection

    // prepare statement
    val statement : PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, id)

    // Execute the query and return the student if found, otherwise None
    val resultSet : ResultSet = statement.executeQuery()

    // check if result set contains any rows
    if (resultSet.next()) {
      // extract student data from the result set.
      val name: String = resultSet.getString("name")
      val username = resultSet.getString("username")
      val email: String = resultSet.getString("email")
      val password: String = resultSet.getString("password")


      // create a student object with the retrieved data
      val student: Student = Student(id, name, username, email, password)

      // close the result set, statement and connection
      resultSet.close()
      statement.close()
      connection.close()

      //return the student object wrapped in Some
      Some(student)

    }
    else {
      // if no student found with the given ID, close the result set.
      resultSet.close()
      statement.close()
      connection.close()

      //return None
      None
    }
  }


  // Update a student's details in the database
  def updateStudent(student: Student): Unit = {
    val sql = "UPDATE students SET name = ?, email= ?, password = ? WHERE id = ?"

    // establish connection
    val connection : Connection = getConnection

    // execute query
    val statement : PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, student.studentId)
    statement.setString(2, student.name)
    statement.setString(3, student.username)
    statement.setString(4, student.email)
    statement.setString(5, student.password)

    // Execute the update statement to modify the student's details
    statement.executeUpdate()

    // close things you opened
    connection.close()
    statement.close()
  }

  // Delete a student from the database
  def deleteStudent(studentId: Int): Unit = {
    val sql = "DELETE FROM students WHERE id = ?"

    // get connection and prepare statement and execute update
    val connection : Connection = getConnection
    val statement : PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, studentId)
    statement.executeUpdate()
    statement.close()
    connection.close()

  }

  // difference between execute executequery and executeupdate
  // Execute query get data from database, execute update is for insert, update or delete.



  // Inserts a new feedback entry into the database
  def insertFeedback(feedback: Feedback): Unit = {
    val sql = "INSERT INTO feedback (student_id, message) VALUES (?, ?)"
    val connection : Connection = getConnection

    try {
      val statement: PreparedStatement = connection.prepareStatement(sql)
      statement.setInt(1, feedback.studentId)
      statement.setString(2, feedback.message)

      statement.executeUpdate()
    } catch {
      case e : SQLException=>
        println("Error")
    } finally {
      connection.close()
    }
  }

  // Retrieves all feedback given by a specific student
  def getFeedbackByStudentId(studentId: Int): Seq[Feedback] = {
    val sql = "SELECT * FROM feedback WHERE student_id =?"
    val connection : Connection = getConnection

    try {
      val statement : PreparedStatement = connection.prepareStatement(sql)
      statement.setInt(1, studentId)
      val resultSet : ResultSet = statement.executeQuery()
      val feeedbackList = extractFeedback(resultSet)
      resultSet.close()
      statement.close()
      feeedbackList

    } catch {
      case e : SQLException =>
        println("Error : " + e.getMessage)
        Seq.empty[Feedback]
    } finally {
      connection.close()
    }
  }

  // Inserts a new book issue record into the database
  def insertBookIssue(issue: IssueBook): Unit = {
    val sql = "INSERT INTO book_issues (student_id, book_id, issue_date, return_date) VALUES (?,?,?,?)"

    val connection: Connection = getConnection

    try {

      val statement: PreparedStatement = connection.prepareStatement(sql)
      statement.setInt(1, issue.issueId)
      statement.setString(2, issue.bookId)
      statement.setDate(3, new java.sql.Date(issue.issueDate.getTime))
      statement.setDate(4, issue.returnDate.map(date => new java.sql.Date(date.getTime)).orNull)
      statement.executeUpdate()
    } catch {
      case e : SQLException =>
        println("Error " + e)
    } finally {
      connection.close()
    }

  }

  // Retrieves all book issues made by a specific student
  def getBookIssuesByStudentId(studentId: Int): Seq[IssueBook] = {
    val sql = "SELECT * FROM book_issue (student_id) WHERE (?)"
    val connection: Connection = getConnection

    try {
      val statement: PreparedStatement = connection.prepareStatement(sql)
      statement.setInt(1, studentId)
      val resultSet: ResultSet = statement.executeQuery()
      extractBookIssues(resultSet)
    } catch {
      case e : SQLException =>
        Seq.empty[IssueBook]
    } finally {
      connection.close()
    }
  }


  // Updates a student's password in the database
  def changeStudentPassword(studentId: Int, newPassword: String): Unit = {
    var sql = "UPDATE students SET password = ? WHERE id = ? "
    // update students (list) SET password to (?input) WHERE id = (?input)

    // connection and prepare statement
    val connection: Connection = getConnection
    val statement : PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, studentId)
    statement.setString(2, newPassword)

    // execute statement
    statement.executeUpdate()

    // close statement and connection
    statement.close()
    connection.close()

  }

  def getBooksByStudentId(studentId: Int): List[Book] = {
    val sql = "SELECT * FROM issue_book WHERE student_id = ?"
    val connection: Connection = getConnection
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, studentId)
    val resultSet: ResultSet = statement.executeQuery()

    val books = new ListBuffer[Book]()
    while (resultSet.next()) {
      val bookId = resultSet.getString("book_id")
      val book = getBookById(bookId)
      book.foreach(books += _)
    }

    books.toList
  }



  def getBookById(bookId: String): Option[Book] = {
    val sql = "SELECT * FROM book WHERE book_id = ?"
    val connection: Connection = getConnection
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, bookId)
    val resultSet: ResultSet = statement.executeQuery()

    if (resultSet.next()) {
      val book = Book(
        resultSet.getString("book_id"),
        resultSet.getString("title"),
        resultSet.getString("author"),
        resultSet.getString("category"),
        resultSet.getString("status")
      )
      Some(book)
    } else {
      None
    }
  }

  def issueBook(bookId: String, studentId: Int): Unit = {
    val sqlUpdate = "UPDATE book SET status = 'Issued', issued_to = ? WHERE book_id = ?"
    val connection: Connection = getConnection
    val statement: PreparedStatement = connection.prepareStatement(sqlUpdate)
    statement.setInt(1, studentId)
    statement.setString(2, bookId)

    val rowsUpdated = statement.executeUpdate()

    if (rowsUpdated > 0) {
      println("Book issued successfully.")
    } else {
      println("Failed to issue the book.")
    }

    statement.close()
    connection.close()
  }




  def updateBookStatus(bookId: String, newStatus: String): Unit = {
    val sql = "UPDATE book SET status = ? WHERE book_id = ?"
    val connection: Connection = getConnection
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, newStatus)
    statement.setString(2, bookId)
    statement.executeUpdate()
    connection.close()
  }


  def getBookStatusesByStudentId(studentId: Int): Seq[BookStatus] = {
    val sql = "SELECT * FROM book_status WHERE student_id = ?"

    // Get connection and prepare statement
    val connection: Connection = getConnection
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setInt(1, studentId)

    // Execute the query and obtain the result set
    val resultSet: ResultSet = statement.executeQuery()

    // Map each row of the result set to a BookStatus object
    val bookStatuses: Seq[BookStatus] = Iterator.continually(resultSet)
      .takeWhile(_.next())
      .map(row => BookStatus(row.getInt("book_id"), row.getString("status")))
      .toSeq

    // Close the result set, statement, and connection
    resultSet.close()
    statement.close()
    connection.close()

    bookStatuses
  }




  private def extractBookStatuses(resultSet: ResultSet): Seq[BookStatus] = {
    var bookStatuses: Seq[BookStatus] = Seq.empty

    while (resultSet.next()) {
      val bookId = resultSet.getInt("book_id")
      val book_status = resultSet.getString("book_status")

      val bookStatus = BookStatus(bookId, book_status)
      bookStatuses = bookStatuses :+ bookStatus
    }

    bookStatuses
  }

  def addBooks(books: List[Book]): Unit = {
    val sql = "INSERT INTO books (book_id, title, author, category, status) VALUES (?, ?, ?, ?, ?)"
    val connection: Connection = getConnection
    val statement: PreparedStatement = connection.prepareStatement(sql)

    books.foreach { book =>
      statement.setString(1, book.bookId)
      statement.setString(2, book.title)
      statement.setString(3, book.author)
      statement.setString(4, book.category)
      statement.setString(5, book.status)
      statement.addBatch()
    }

    statement.executeBatch()
    statement.close()
    connection.close()

    println("Books added successfully!")
  }

  def viewAllBooks(): Unit = {
    val sql = "SELECT * FROM book"
    val connection: Connection = getConnection
    val statement: Statement = connection.createStatement()
    val resultSet: ResultSet = statement.executeQuery(sql)

    println("=================================")
    println("           ALL BOOKS            ")
    println("=================================")
    while (resultSet.next()) {
      val bookId = resultSet.getString("book_id")
      val title = resultSet.getString("title")
      val author = resultSet.getString("author")
      val category = resultSet.getString("category")
      val status = resultSet.getString("status")

      println("Book ID: " + bookId)
      println("Title: " + title)
      println("Author: " + author)
      println("Category: " + category)
      println("Status: " + status)
      println("---------------------------------")
    }

    statement.close()
    connection.close()
  }



  def deleteBook(bookId: String): Unit = {
    val sql = "DELETE FROM book WHERE book_id = ?"
    val connection: Connection = getConnection
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, bookId)
    val rowsDeleted: Int = statement.executeUpdate()

    if (rowsDeleted > 0) {
      println("Book deleted successfully!")
    } else {
      println("Failed to delete book. Book not found.")
    }

    statement.close()
    connection.close()
  }







  // EXTRACT SECTION

  def extractStudents(resultSet: ResultSet): List[Student] = {
    val students = new ListBuffer[Student]()

    while (resultSet.next()) {
      val studentId = resultSet.getInt("student_id")
      val name = resultSet.getString("name")
      val username = resultSet.getString("username")
      val email = resultSet.getString("email")
      val password = resultSet.getString("password")

      val student = Student(studentId, name, username, email, password)
      students += student
    }

    students.toList
  }

  // Helper methods to extract data from ResultSet
  def extractFeedback(resultSet: ResultSet): List[Feedback] = {
    var feedbackList: List[Feedback] = List.empty

    // Extract feedback records from the result set and populate the feedbackList

    while (resultSet.next()) {
      val feedback = Feedback(
        resultSet.getInt("student_id"),
        resultSet.getString("message"),
      )
      feedbackList = feedbackList :+ feedback
    }

    feedbackList
  }


  private def extractBookIssues(resultSet: ResultSet): Seq[IssueBook] = {
    // Implementation to extract BookIssue objects from ResultSet
    val bookIssues: Seq[IssueBook] = Seq.empty

    while (resultSet.next()) {
      val studentId = resultSet.getInt("student_id: ")
      val bookId = resultSet.getString("book_id: ")
      val issueDate = resultSet.getDate("issue_date: ")
      val returnDate = Option(resultSet.getDate("return_date: "))
      val bookIssue = IssueBook(studentId, bookId, issueDate, returnDate)
      bookIssues :+ bookIssue
    }
    bookIssues
  }

  def extractBooks(resultSet: ResultSet): List[Book] = {
    val books = new ListBuffer[Book]()

    while (resultSet.next()) {
      val bookId = resultSet.getString("book_id")
      val title = resultSet.getString("title")
      val author = resultSet.getString("author")
      val category = resultSet.getString("category")
      val status = resultSet.getString("status")

      val book = new Book(bookId, title, author, category, status)
      books += book
    }

    books.toList
  }

  def extractCategories(resultSet: ResultSet): List[Category] = {
    val categories = new ListBuffer[Category]()

    while (resultSet.next()) {
      val categoryId = resultSet.getString("category_id")
      val categoryName = resultSet.getString("category_name")

      val category = new Category(categoryId, categoryName)
      categories += category
    }

    categories.toList
  }



  def manageStudents(): Unit = {
    println("=================================")
    println("     Manage Students")
    println("=================================")
    println("1. Add New Student")
    println("2. Edit Existing Student")
    println("3. View Student Profile")
    println("4. List All Students")
    println("5. Assign Roles to Student for Login")
    println("6. Create Login Credentials for Student")
    println("=================================")

    val choice = scala.io.StdIn.readInt()
    val student = Student(2, "adam", "adam","aedamjung@gmail.com","123")

    choice match {
      case 1 =>
        insertStudent(student)
      case 2 =>
        updateStudent(student)
      case 3 =>
        println("enter id : ")
        val id = StdIn.readInt()
        getStudentById(id)
      case 4 =>
        getAllStudents()
      case 5 =>
        println("enter id : ")
        val id = StdIn.readInt()
        getBookStatusesByStudentId(id)
      case 6 =>
        println("enter id : ")
        val id = StdIn.readInt()
        getBookStatusesByStudentId(id)
      case 7 =>
        println("enter id : ")
        val id = StdIn.readInt()
        getFeedbackByStudentId(id)
      case _ =>
        println("Invalid choice")
    }
  }

  def manageBooks(): Unit = {
    println("=================================")
    println("      Manage Books")
    println("=================================")
    println("1. Add Books")
    println("2. Change Status of Books")
    println("3. Delete Books")
    println("4. Reply on Books")
    println("5. Search Books")
    println("=================================")

    val choice = scala.io.StdIn.readInt()
    val books : List[Book] = List(

      Book("B01","Harry Potter and the prisoner of Azkaban", "RKJ", "Adventure","available"),
      Book("B02","Harry Potter and the Deathly Hollows", "RKJ", "Adventure","available")

    )

    choice match {
      case 1 =>
        addBooks(books)
      case 2 =>
        println("enter id : ")
        val bookId = StdIn.readLine()
        println("enter status : ")
        val bookStatus = StdIn.readLine()
        updateBookStatus(bookId,bookStatus)
      case 3 =>
        println("enter id : ")
        val bookId = StdIn.readLine()
        deleteBook(bookId)
      case 4 =>
        viewAllBooks()
      case 5 =>
        println("enter id : ")
        val bookId = StdIn.readLine()
        getBookById(bookId)
      case _ =>
        println("Invalid choice")
    }
  }


  def manageCategory(): Unit = {
    println("=================================")
    println("     Manage Category")
    println("=================================")
    println("1. Add Category")
    println("2. Edit Category")
    println("3. List All Categories")
    println("=================================")

    val choice = scala.io.StdIn.readInt()
    val category = Classes.Category("ID123", "Adventure")
    choice match {
      case 1 =>
        println("enter category id : ")
        val catId = StdIn.readLine()
        println("enter category name : ")
        val catName = StdIn.readLine()
        category.addCategory(catId, catName)
      case 2 =>
        println("enter category id : ")
        val catId = StdIn.readLine()
        println("enter new category name : ")
        val catName = StdIn.readLine()
        category.editCategory(catId, catName)
      case 3 =>
        println("enter category id : ")
        val catId = StdIn.readLine()
        category.deleteCategory(catId)
      case 4 =>
        category.viewAllCategories()
      case 5 =>
        println("enter category id : ")
        val catId = StdIn.readLine()
        category.getCategoryById(catId)
      case _ =>
        println("Invalid choice")
    }
  }

  def manageIssueBooks(): Unit = {
    println("=================================")
    println("    Manage Issue Books")
    println("=================================")
    println("1. Add Issue Books")
    println("2. Edit Issue Books")
    println("3. List All Issue Books")
    println("=================================")

    val choice = scala.io.StdIn.readInt()

    // 1st issuebook

    val bookId = "BOOK001"
    val issueId = 1
    val issueDate = Date.valueOf("2023-05-31")
    val returnDate = Some(Date.valueOf("2023-06-07"))
    val issueBook = new IssueBook(issueId, bookId, issueDate, returnDate)


    choice match {
      case 1 =>
        println("Enter Issue ID:")
        var issueId: Int = scala.io.StdIn.readInt()

        println("Enter Book ID:")
        val bookId: String = scala.io.StdIn.readLine()

        println("Enter Issue Date (yyyy-MM-dd):")
        val issueDateStr: String = scala.io.StdIn.readLine()
        val issueDate: Date = Date.valueOf(issueDateStr)

        println("Enter Return Date (yyyy-MM-dd) or leave empty:")
        val returnDateStr: String = scala.io.StdIn.readLine()
        val returnDate: Option[Date] = if (returnDateStr.nonEmpty) Some(Date.valueOf(returnDateStr)) else None

        val issueBook = IssueBook(
          issueId = issueId,
          bookId = bookId,
          issueDate = issueDate,
          returnDate = returnDate
        )
        issueBook.addIssueBook(bookId, issueId, issueDate, returnDate)
      case 2 =>

        println("Enter Issue ID:")
        var issueId: Int = scala.io.StdIn.readInt()

        println("Enter Book ID:")
        val bookId: String = scala.io.StdIn.readLine()

        println("Enter Issue Date (yyyy-MM-dd):")
        val issueDateStr: String = scala.io.StdIn.readLine()
        val issueDate: Date = Date.valueOf(issueDateStr)

        println("Enter Return Date (yyyy-MM-dd) or leave empty:")
        val returnDateStr: String = scala.io.StdIn.readLine()
        val returnDate: Option[Date] = if (returnDateStr.nonEmpty) Some(Date.valueOf(returnDateStr)) else None

        val issueBook = IssueBook(
          issueId = issueId,
          bookId = bookId,
          issueDate = issueDate,
          returnDate = returnDate
        )

        issueBook.editIssueBook(issueId, bookId,returnDate , issueDate )
      case 3 =>
        println("Enter Issue ID:")
        var issueId: Int = scala.io.StdIn.readInt()

        issueBook.deleteIssueBook(issueId)
      case 4 =>
        issueBook.viewAllIssueBook()
      case 5 =>
        println("Enter Issue ID:")
        var issueId: Int = scala.io.StdIn.readInt()

        issueBook.getIssueBookById(issueId)
      case _ =>
        println("Invalid choice")
    }
  }

  def manageReports(): Unit = {
    println("=================================")
    println("       Manage Reports")
    println("=================================")
    println("1. Report of All Students")
    println("2. Report of All Books")
    println("3. Report of All Categories")
    println("4. Report of All Issue Books")
    println("=================================")

    val choice = scala.io.StdIn.readInt()
    val databaseManager = new DatabaseManager()
    val report = Reports(databaseManager)


    choice match {
      case 1 =>
        report.reportAllStudents()
      case 2 =>
        report.reportAllBooks()
      case 3 =>
        report.reportAllCategories()
      case 4 =>
        report.reportAllIssueBooks()
      case 5 =>
        report.reportAllFeedback()
      case _ =>
        println("Invalid choice")
    }
  }


  def getAllStudents(): List[Student] = {
    val sql = "SELECT * FROM students"
    val connection: Connection = getConnection
    val statement = connection.createStatement()
    val resultSet: ResultSet = statement.executeQuery(sql)

    var students: List[Student] = List.empty

    while (resultSet.next()) {
      val student = Student(
        resultSet.getInt("student_id"),
        resultSet.getString("name"),
        resultSet.getString("username"),
        resultSet.getString("email"),
        resultSet.getString("password"),
      )
      students = students :+ student
    }

    students
  }

  // REPORTS SECTION

  case class Reports(databaseManager : DatabaseManager) {
    def reportAllStudents(): Unit = {
      val sql = "SELECT * FROM student"
      val connection: Connection = getConnection
      val statement: PreparedStatement = connection.prepareStatement(sql)
      val resultSet: ResultSet = statement.executeQuery()

      val students = databaseManager.extractStudents(resultSet)

      println("=================================")
      println("+       STUDENT REPORT           +")
      println("=================================")

      for (student <- students) {
        println("Student ID: " + student.studentId)
        println("Name: " + student.name)
        println("Age: " + student.username)
        println("Email: " + student.email)
        println("---------------------------------")
      }

      println("Total students: " + students.length)
      println("=================================")
    }


    def reportAllBooks(): Unit = {
      val sql = "SELECT * FROM book"
      val connection: Connection = getConnection
      val statement: PreparedStatement = connection.prepareStatement(sql)
      val resultSet: ResultSet = statement.executeQuery()

      val books = databaseManager.extractBooks(resultSet)

      println("=================================")
      println("+          BOOK REPORT           +")
      println("=================================")

      for (book <- books) {
        println("Book ID: " + book.bookId)
        println("Title: " + book.title)
        println("Author: " + book.author)
        println("Category: " + book.category)
        println("Status: " + book.status)
        println("---------------------------------")
      }

      println("Total books: " + books.length)
      println("=================================")
    }

    def reportAllCategories(): Unit = {
      val sql = "SELECT * FROM category"
      val connection: Connection = getConnection
      val statement: PreparedStatement = connection.prepareStatement(sql)
      val resultSet: ResultSet = statement.executeQuery()

      val categories = databaseManager.extractCategories(resultSet)

      println("=================================")
      println("+       CATEGORY REPORT          +")
      println("=================================")

      for (category <- categories) {
        println("Category ID: " + category.categoryName)
        println("Name: " + category.categoryName)
        println("---------------------------------")
      }

      println("Total categories: " + categories.length)
      println("=================================")
    }

    def reportAllIssueBooks(): Unit = {
      val sql = "SELECT * FROM book_issues"
      val connection: Connection = getConnection
      val statement: PreparedStatement = connection.prepareStatement(sql)
      val resultSet: ResultSet = statement.executeQuery()
      val issueBooks = databaseManager.extractBookIssues(resultSet)

      println("=================================")
      println("+      ISSUE BOOK REPORT         +")
      println("=================================")

      for (issueBook <- issueBooks) {
        println("Issue ID: " + issueBook.issueId)
        println("Book ID: " + issueBook.bookId)
        println("Issue Date: " + issueBook.issueDate)
        println("Return Date: " + issueBook.returnDate)
        println("---------------------------------")
      }

      println("Total issue books: " + issueBooks.length)
      println("=================================")
    }


    def reportAllFeedback(): Unit = {
      val sql = "SELECT * FROM feedback"
      val connection: Connection = getConnection
      val statement: PreparedStatement = connection.prepareStatement(sql)
      val resultSet: ResultSet = statement.executeQuery()
      val feedbackList = databaseManager.extractFeedback(resultSet)

      println("=================================")
      println("+       FEEDBACK REPORT          +")
      println("=================================")

      for (feedback <- feedbackList) {
        println("Feedback ID: " + feedback.message)
        println("Student ID: " + feedback.studentId)
        println("---------------------------------")
      }

      println("Total feedback records: " + feedbackList.length)
      println("=================================")
    }
  }



}
