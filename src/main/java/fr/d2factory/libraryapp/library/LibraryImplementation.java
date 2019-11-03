package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;

public class LibraryImplementation implements Library {

	BookRepository bookRepo;

	/**
	 * Constructor
	 * 
	 * @param bookRepository
	 */

	public LibraryImplementation(BookRepository bookRepository) {

		this.bookRepo = bookRepository;
	}

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		Book bookIfAvailable = bookRepo.findBook(isbnCode);
		LocalDate bookIfBorrowed = bookRepo.findBorrowedBook(isbnCode);
		if (bookIfAvailable == null && bookIfBorrowed == null) {

			throw new LibraryException("This book dont exist in our library");
		}
		if (!member.canBorrowBooks()) {

			throw new HasLateBooksException("This member has already books in late");
		}
		if (bookIfBorrowed != null) {

			throw new LibraryException("This book is already taken");
		}
		bookRepo.saveBookBorrow(bookIfAvailable, borrowedAt);
		member.addBooktoMemberBorrowedBooks(bookIfAvailable, borrowedAt);
		return bookIfAvailable;
	}

	@Override
	public void returnBook(Book book, Member member) {

		member.payBook((int) ChronoUnit.DAYS.between(bookRepo.findBorrowedBookDate(book), LocalDate.now()));
		member.removeBooktoMemberBorrowedBooks(book);
		bookRepo.removeBookBorrow(book);
	}

}
