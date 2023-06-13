package LibraryManagement.Main.Classes
import LibraryManagement.Main.Database.DatabaseManager

case class Book(bookId: String, title: String, author: String, category: String, status: String)
{

  def checkBookStatus(books: List[Book]): Unit = {
    println("Book Status:")
    books.foreach {
      book =>
        println(s"${book.title}: ${book.status}")
    }
  }
}
