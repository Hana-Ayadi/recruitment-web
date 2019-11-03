package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
	private Map<ISBN, Book> availableBooks = new HashMap<>();
	private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

	public void addBooks(List<Book> books) {

		for (int i = 0; i < books.size(); i++) {

			availableBooks.put(books.get(i).getIsbn(), books.get(i));

		}

	}

	public Book findBook(long isbnCode) {

		for (Map.Entry<ISBN, Book> entry : availableBooks.entrySet()) {

			if (entry.getKey().getIsbnCode() == isbnCode) {

				return availableBooks.get(entry.getKey());
			}
		}

		return null;
	}

	public LocalDate findBorrowedBook(long isbnCode) {

		for (Map.Entry<Book, LocalDate> entry : borrowedBooks.entrySet()) {

			if (entry.getKey().getIsbn().getIsbnCode() == isbnCode) {

				return borrowedBooks.get(entry.getKey());
			}
		}

		return null;
	}

	public void saveBookBorrow(Book book, LocalDate borrowedAt) {

		borrowedBooks.put(book, borrowedAt);
		availableBooks.remove(book.getIsbn());

	}

	public void removeBookBorrow(Book book) {

		borrowedBooks.remove(book);
		availableBooks.put(book.getIsbn(), book);

	}

	public LocalDate findBorrowedBookDate(Book book) {

		return borrowedBooks.get(book);
	}

}
