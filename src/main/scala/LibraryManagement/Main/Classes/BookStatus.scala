package LibraryManagement.Main.Classes

case class BookStatus(bookId: Int, status: String) {


  def issue( status: String): Unit = {
    if (status == "Available") {
      val status = "Issued"
      println("Book issued successfully!")
    } else {
      println("Book is not available for issuing.")
    }
  }

  def displayStatus(): Unit = {
    // TODO: Implement logic to display the status of the book
    println(s"Book ID: $bookId")
    println(s"Status: $status")
  }
}
