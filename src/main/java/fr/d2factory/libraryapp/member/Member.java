package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library} A
 * member can be either a student or a resident
 */
public abstract class Member {
	/**
	 * An initial sum of money the member has
	 */
	private float wallet;
	private int nb;
	private Map<Book, LocalDate> memberBorrowedBooks = new HashMap<>();

	public Member(float wallet, int nb) {
		super();
		this.wallet = wallet;
		this.nb = nb;
	}

	/**
	 * The member should pay their books when they are returned to the library
	 *
	 * @param numberOfDays the number of days they kept the book
	 */
	public abstract void payBook(int numberOfDays);

	public float getWallet() {
		return wallet;
	}

	public void setWallet(float wallet) {
		this.wallet = wallet;
	}

	public Map<Book, LocalDate> getMemberBorrowedBooks() {
		return memberBorrowedBooks;
	}

	public void setMemberBorrowedBooks(Map<Book, LocalDate> memberBorrowedBooks) {
		this.memberBorrowedBooks = memberBorrowedBooks;
	}

	public void addBooktoMemberBorrowedBooks(Book book, LocalDate borrowedAt) {
		memberBorrowedBooks.put(book, borrowedAt);
	}

	public void removeBooktoMemberBorrowedBooks(Book book) {
		memberBorrowedBooks.remove(book);
	}

	public Boolean canBorrowBooks() {

		for (Entry<Book, LocalDate> entry : getMemberBorrowedBooks().entrySet()) {
			if ((int) ChronoUnit.DAYS.between(entry.getValue(), LocalDate.now()) > nb) {

				return false;

			}
		}

		return true;
	}

}
