package fr.d2factory.libraryapp.library;

/**
 * This exception is thrown when a member who owns late books tries to borrow
 * another book
 */
public class HasLateBooksException extends RuntimeException {

	/**
	 * Exception for members who have late books not yet returned
	 */
	private static final long serialVersionUID = 1L;

	HasLateBooksException(String message) {

		super(message);
	}
}
