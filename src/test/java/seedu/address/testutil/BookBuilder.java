package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.book.Author;
import seedu.address.model.book.Book;
import seedu.address.model.book.BookName;
import seedu.address.model.book.Rating;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Book objects.
 */
public class BookBuilder {

    public static final String DEFAULT_NAME = "Harry Potter and the Deathly Hallows";
    public static final String DEFAULT_AUTHOR = "JK Rowling";
    public static final String DEFAULT_RATING = "7";

    private BookName bookName;
    private Author author;
    private Rating rating;
    private Set<Tag> tags;

    public BookBuilder() {
        bookName = new BookName(DEFAULT_NAME);
        author = new Author(DEFAULT_AUTHOR);
        rating = new Rating(DEFAULT_RATING);
        tags = new HashSet<>();
    }

    /**
     * Initializes the BookBuilder with the data of {@code bookToCopy}.
     */
    public BookBuilder(Book bookToCopy) {
        bookName = bookToCopy.getBookName();
        author = bookToCopy.getAuthor();
        rating = bookToCopy.getRating();
        tags = new HashSet<>(bookToCopy.getTags());
    }

    /**
     * Sets the {@code BookName} of the {@code Book} that we are building.
     */
    public BookBuilder withName(String name) {
        this.bookName = new BookName(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Book} that we are building.
     */
    public BookBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Author} of the {@code Book} that we are building.
     */
    public BookBuilder withAuthor(String author) {
        this.author = new Author(author);
        return this;
    }

    /**
     * Sets the {@code Rating} of the {@code Book} that we are building.
     */
    public BookBuilder withRating(String rating) {
        this.rating = new Rating(rating);
        return this;
    }

    public Book build() {
        return new Book(bookName, author, rating, tags);
    }

}
