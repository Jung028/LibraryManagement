package LibraryManagement.Main.Classes

import LibraryManagement.Main.Database.DatabaseManager

import scala.io.StdIn

// class vs case class
// 1. You need to create a instance of class for just Class,
// while case class no need to create new Student()


  // Other properties and methods of the Student class
case class Student(studentId: Int, name: String, username: String, email: String, password: String)

  case class StudentManager(databaseManager: DatabaseManager) {

    def register(): Unit = {
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

    def updateDetails(): Unit = {
      println("---------------------------")
      println("Update Student Details")
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
        println("Student details updated successfully!")
      } catch {
        case e: Exception =>
          println(s"An error occurred while updating student details: ${e.getMessage}")
      }
    }


    def viewDetails(studentId : Int): Unit = {
      try {
        // Retrieve the student details from the database using the DatabaseManager
        val student = databaseManager.getStudentById(studentId)

        student match {
          case Some(student) =>
            println("Student Details:")
            println(s"ID: ${student.studentId}")
            println(s"Name: ${student.name}")
            println(s"Email: ${student.email}")

          case None =>
            println("Student details not found.")
        }
      } catch {
        case e: Exception =>
          println(s"An error occurred while viewing student details: ${e.getMessage}")
      }
    }


    def changePassword(studentId: Int, newPassword: String): Unit = {

      try {
        // Update the student's password in the database or perform any other required operations
        databaseManager.updateStudentPassword(studentId, newPassword)
        println("Password changed successfully!")
      } catch {
        case e: Exception => println(s"An error occurred while changing the password: ${e.getMessage}")
      }
    }

    def listOwnedBooks(studentId : Int): Unit = {
      // Retrieve the books owned by the student from the database
      val ownedBooks = databaseManager.getBooksByStudentId(studentId)

      if (ownedBooks.nonEmpty) {
        println("Books owned by the student:")
        ownedBooks.foreach(book => println(s"- ${book}"))
      } else {
        println("The student does not own any books.")
      }
    }

    def checkBookStatus(bookId: String): Unit = {
      // Retrieve the status of the book from the database
      val bookStatus = databaseManager.getBookById(bookId)

      bookStatus match {
        case Some(status) =>
          println(s"The status of the book with ID $bookId is: $status")
        case None =>
          println(s"The book with ID $bookId does not exist.")
      }
    }


    def giveFeedback(book: Int): Unit = {
      println(s"Give feedback for book: $book")

      // Prompt the user for feedback input
      println("Enter your feedback:")
      val feedback = scala.io.StdIn.readLine()

      // Create a Feedback object
      val newFeedback = Feedback(book, feedback)

      // Insert the feedback into the database
      databaseManager.insertFeedback(newFeedback)

      println("Feedback submitted successfully.")
    }


  }

