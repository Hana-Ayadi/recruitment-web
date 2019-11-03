package fr.d2factory.libraryapp.library;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

public class LibraryTest {
	private Library library;
	private BookRepository bookRepository;
	List<Book> booksInTheLibrary;

	@Before
	public void setup() throws URISyntaxException, ParseException {
		// TODO instantiate the library and the repository
		bookRepository = new BookRepository();
		library = new LibraryImplementation(bookRepository);

		// TODO add some test books (use BookRepository#addBooks)
		// TODO to help you a file called books.json is available in src/test/resources

		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();
		Path path = Paths.get(getClass().getClassLoader().getResource("books.json").toURI());
		try (FileReader reader = new FileReader(path.toString())) {
			// Read JSON file
			Object object = jsonParser.parse(reader);

			JSONArray books = (JSONArray) object;

			booksInTheLibrary = new ArrayList<>();

			for (Object o : books) {

				JSONObject bookJson = (JSONObject) o;
				String title = (String) bookJson.get("title");
				String author = (String) bookJson.get("author");
				long isbn = (long) ((JSONObject) bookJson.get("isbn")).get("isbnCode");
				Book book = new Book(title, author, new ISBN(isbn));
				booksInTheLibrary.add(book);
			}

			bookRepository.addBooks(booksInTheLibrary);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void member_can_borrow_a_book_if_book_is_available() {
		//System.out.println("This book dont exist in our library");
		thrown.expect(LibraryException.class);
		thrown.expectMessage("This book dont exist in our library");
		Member resident = new Resident(8);
		library.borrowBook(3326456007846L, resident, LocalDate.now().minusDays(20));

	}

	@Test
	public void borrowed_book_is_no_longer_available() {
		thrown.expect(LibraryException.class);
		thrown.expectMessage("This book is already taken");
		Member resident = new Resident(8);
		library.borrowBook(968787565445L, resident, LocalDate.now().minusDays(20));
		Member student1 = new Student(4, 2017);
		library.borrowBook(968787565445L, student1, LocalDate.now().minusDays(10));

	}

	@Test
	public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
		Member resident = new Resident(8);
		library.borrowBook(booksInTheLibrary.get(0).getIsbn().getIsbnCode(), resident, LocalDate.now().minusDays(20));
		library.returnBook(booksInTheLibrary.get(0), resident);
		Assert.assertEquals(6, resident.getWallet(), 0);
	}

	@Test
	public void students_pay_10_cents_the_first_30days() {
		Member student1 = new Student(4, 2017);
		library.borrowBook(booksInTheLibrary.get(0).getIsbn().getIsbnCode(), student1, LocalDate.now().minusDays(10));
		library.returnBook(booksInTheLibrary.get(0), student1);
		Assert.assertEquals(3, student1.getWallet(), 0);
	}

	@Test
	public void students_in_1st_year_are_not_taxed_for_the_first_15days() {

		Member student1 = new Student(4, 2019);
		library.borrowBook(booksInTheLibrary.get(0).getIsbn().getIsbnCode(), student1, LocalDate.now().minusDays(30));
		library.returnBook(booksInTheLibrary.get(0), student1);
		Assert.assertEquals(2.5, student1.getWallet(), 0);

	}

	@Test
	public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {
		Member student1 = new Student(8, 2019);
		library.borrowBook(booksInTheLibrary.get(0).getIsbn().getIsbnCode(), student1, LocalDate.now().minusDays(40));
		library.returnBook(booksInTheLibrary.get(0), student1);
		Assert.assertEquals(5, student1.getWallet(), 0);
	}

	@Test
	public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
		Member resident = new Resident(9);
		library.borrowBook(booksInTheLibrary.get(0).getIsbn().getIsbnCode(), resident, LocalDate.now().minusDays(70));
		library.returnBook(booksInTheLibrary.get(0), resident);
		Assert.assertEquals(1, resident.getWallet(), 0);
	}

	@Test
	public void members_cannot_borrow_book_if_they_have_late_books() {
		thrown.expect(HasLateBooksException.class);
		thrown.expectMessage("This member has already books in late");
		Member resident = new Resident(20);
		library.borrowBook(booksInTheLibrary.get(0).getIsbn().getIsbnCode(), resident, LocalDate.now().minusDays(70));
		library.borrowBook(booksInTheLibrary.get(1).getIsbn().getIsbnCode(), resident, LocalDate.now().minusDays(5));

	}
}
