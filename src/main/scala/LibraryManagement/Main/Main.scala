package LibraryManagement.Main

import Database.DatabaseManager
import LibraryManagement.Main.Classes.{Book, Student, StudentManager}

import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = {
    val databaseManager = new DatabaseManager()

    //Get user role
    println("Are you a student or admin? : ")
    val role = StdIn.readLine().toLowerCase()

    if (role == "student") {
      // login to student ask for id and password
      val studentLogin = new databaseManager.StudentLogin(databaseManager)
      studentLogin.login()
      var loggedIn = false
      var exit = false

      while (!exit) {
        if (!loggedIn) {
          studentLogin.listStudentActions()
          println("Enter your choice: ")
          val choice = StdIn.readInt()
          val student = Student(1, "Adam", "AEDAM","aedamjung@gmail.com", "1234")


          choice match {
            case 1 =>
              // Create an instance of the DatabaseManager class
              val databaseManager = new DatabaseManager()

              // Create an instance of the StudentManager class
              val studentManager = StudentManager(databaseManager)

              // Call the register() method on the studentManager instance
              studentManager.register()
              databaseManager.insertStudent(student)
            case 2 =>
              studentLogin.login()
              loggedIn = true
            case 3 =>
              println("Enter bookId: ")
              val bookId = StdIn.readInt()

              // Create an instance of the DatabaseManager class
              val databaseManager = new DatabaseManager()

              // Create an instance of the StudentManager class
              val studentManager = StudentManager(databaseManager)

              // Call the register() method on the studentManager instance
              studentManager.giveFeedback(bookId)
            case 4 =>
              println("Enter bookId: ")
              val bookId = StdIn.readLine()
              println("Enter student Id: ")
              val studentId = StdIn.readInt()
              databaseManager.issueBook(bookId, studentId)

            case 5 =>
              println("Enter student Id: ")
              val studentId = StdIn.readInt()
              println("Enter new password: ")
              val password = StdIn.readLine()
              databaseManager.updateStudentPassword(studentId,password)
            case 6 =>
              databaseManager.viewAllBooks()
            case 7 =>
              val book = Book("ID123", "D", "salamandar","sci-FI","okay")
              val books: List[Book] = List(
                 Book("ID123", "D", "salamandar","sci-FI","okay"),
                 Book("ID123", "D", "salamandar","sci-FI","okay"),
                 Book("ID123", "D", "salamandar","sci-FI","okay")
              )
              book.checkBookStatus(books)
            case 8 =>
              //bookId: String, newStatus: String
              println("Enter book Id: ")
              val bookId = StdIn.readLine()
              println("Enter new status: ")
              val newStatus = StdIn.readLine()
              databaseManager.updateBookStatus(bookId,newStatus)
            case 9 =>
              loggedIn = false
              exit = true
            case _ => println("Invalid choice. Please try again.")
          }
        } else {
          // Handle logged in student actions
          // ...

          loggedIn = false
        }
      }
    } else if (role =="admin") {
      // login for admin
      val adminLogin = new databaseManager.AdminLogin(databaseManager)
      adminLogin.login()
       adminLogin.listAdminActions()
      // allow options for managing reports
        databaseManager.manageReports()
      // then show actions available
    } else {
      println("Invalid role")
    }
  }
}
