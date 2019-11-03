package fr.d2factory.libraryapp.library;

public class LibraryException extends RuntimeException {

	/**
	 * Exception for book when it is already taken 
	 */
	private static final long serialVersionUID = 1L;

	LibraryException(String message) {

		super(message);
	}

}
