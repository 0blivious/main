package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalBooks.ALI;
import static seedu.address.testutil.TypicalBooks.BOOKTHIEF;
import static seedu.address.testutil.TypicalBooks.CS;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.AddressBookBuilder;

public class VersionedBookShelfTest {

    private final ReadOnlyBookShelf bookShelfWithAlice = new AddressBookBuilder().withBook(ALI).build();
    private final ReadOnlyBookShelf bookShelfWithCS = new AddressBookBuilder().withBook(CS).build();
    private final ReadOnlyBookShelf bookShelfWithThief = new AddressBookBuilder().withBook(BOOKTHIEF).build();
    private final ReadOnlyBookShelf emptyBookShelf = new AddressBookBuilder().build();

    @Test
    public void commit_singleBookList_noStatesRemovedCurrentStateSaved() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(emptyBookShelf);

        versionedBookShelf.commit();
        assertBookShelfListStatus(versionedBookShelf,
                Collections.singletonList(emptyBookShelf),
                emptyBookShelf,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleBookShelfPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);

        versionedBookShelf.commit();
        assertBookShelfListStatus(versionedBookShelf,
                Arrays.asList(emptyBookShelf, bookShelfWithAlice, bookShelfWithCS),
                bookShelfWithCS,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleBookShelfPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 2);

        versionedBookShelf.commit();
        assertBookShelfListStatus(versionedBookShelf,
                Collections.singletonList(emptyBookShelf),
                emptyBookShelf,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleBookShelfPointerAtEndOfStateList_returnsTrue() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);

        assertTrue(versionedBookShelf.canUndo());
    }

    @Test
    public void canUndo_multipleBookShelfPointerAtStartOfStateList_returnsTrue() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 1);

        assertTrue(versionedBookShelf.canUndo());
    }

    @Test
    public void canUndo_singleBookShelf_returnsFalse() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(emptyBookShelf);

        assertFalse(versionedBookShelf.canUndo());
    }

    @Test
    public void canUndo_multipleBookShelfPointerAtStartOfStateList_returnsFalse() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 2);

        assertFalse(versionedBookShelf.canUndo());
    }

    @Test
    public void canRedo_multipleBookShelfPointerNotAtEndOfStateList_returnsTrue() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 1);

        assertTrue(versionedBookShelf.canRedo());
    }

    @Test
    public void canRedo_multipleBookShelfPointerAtStartOfStateList_returnsTrue() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithThief);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 2);

        assertTrue(versionedBookShelf.canRedo());
    }

    @Test
    public void canRedo_singleBookShelf_returnsFalse() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(emptyBookShelf);

        assertFalse(versionedBookShelf.canRedo());
    }

    @Test
    public void canRedo_multipleBookShelfPointerAtEndOfStateList_returnsFalse() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);

        assertFalse(versionedBookShelf.canRedo());
    }

    @Test
    public void undo_multipleBookShelfPointerAtEndOfStateList_success() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);

        versionedBookShelf.undo();
        assertBookShelfListStatus(versionedBookShelf,
                Collections.singletonList(emptyBookShelf),
                bookShelfWithAlice,
                Collections.singletonList(bookShelfWithCS));
    }

    @Test
    public void undo_multipleBookShelfPointerNotAtStartOfStateList_success() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 1);

        versionedBookShelf.undo();
        assertBookShelfListStatus(versionedBookShelf,
                Collections.emptyList(),
                emptyBookShelf,
                Arrays.asList(bookShelfWithAlice, bookShelfWithCS));
    }

    @Test
    public void undo_singleBookShelf_throwsNoUndoableStateException() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(emptyBookShelf);

        assertThrows(VersionedBookShelf.NoUndoableStateException.class, versionedBookShelf::undo);
    }

    @Test
    public void undo_multipleBookShelfPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 2);

        assertThrows(VersionedBookShelf.NoUndoableStateException.class, versionedBookShelf::undo);
    }

    @Test
    public void redo_multipleBookShelfPointerNotAtEndOfStateList_success() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 1);

        versionedBookShelf.redo();
        assertBookShelfListStatus(versionedBookShelf,
                Arrays.asList(emptyBookShelf, bookShelfWithAlice),
                bookShelfWithCS,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleBookShelfPointerAtStartOfStateList_success() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 2);

        versionedBookShelf.redo();
        assertBookShelfListStatus(versionedBookShelf,
                Collections.singletonList(emptyBookShelf),
                bookShelfWithAlice,
                Collections.singletonList(bookShelfWithCS));
    }

    @Test
    public void redo_singleBookShelf_throwsNoRedoableStateException() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(emptyBookShelf);

        assertThrows(VersionedBookShelf.NoRedoableStateException.class, versionedBookShelf::redo);
    }

    @Test
    public void redo_multipleBookShelfPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(
                emptyBookShelf, bookShelfWithAlice, bookShelfWithCS);

        assertThrows(VersionedBookShelf.NoRedoableStateException.class, versionedBookShelf::redo);
    }

    @Test
    public void equals() {
        VersionedBookShelf versionedBookShelf = prepareBookShelfList(bookShelfWithAlice, bookShelfWithCS);

        // same values -> returns true
        VersionedBookShelf copy = prepareBookShelfList(bookShelfWithAlice, bookShelfWithCS);
        assertTrue(versionedBookShelf.equals(copy));

        // same object -> returns true
        assertTrue(versionedBookShelf.equals(versionedBookShelf));

        // null -> returns false
        assertFalse(versionedBookShelf.equals(null));

        // different types -> returns false
        assertFalse(versionedBookShelf.equals(1));

        // different state list -> returns false
        VersionedBookShelf differentBookShelfList = prepareBookShelfList(bookShelfWithCS, bookShelfWithThief);
        assertFalse(versionedBookShelf.equals(differentBookShelfList));

        // different current pointer index -> returns false
        VersionedBookShelf differentCurrentStatePointer = prepareBookShelfList(
                bookShelfWithAlice, bookShelfWithCS);
        shiftCurrentStatePointerLeftwards(versionedBookShelf, 1);
        assertFalse(versionedBookShelf.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedBookList} is currently pointing at {@code expectedCurrentState},
     * states before {@code versionedBookList#currentStatePointer} is equal to {@code expectedStatesBeforePointer},
     * and states after {@code versionedBookList#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertBookShelfListStatus(VersionedBookShelf versionedBookShelf,
                                             List<ReadOnlyBookShelf> expectedStatesBeforePointer,
                                             ReadOnlyBookShelf expectedCurrentState,
                                             List<ReadOnlyBookShelf> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new BookShelf(versionedBookShelf), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedBookShelf.canUndo()) {
            versionedBookShelf.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyBookShelf expectedBookShelf : expectedStatesBeforePointer) {
            assertEquals(expectedBookShelf, new BookShelf(versionedBookShelf));
            versionedBookShelf.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyBookShelf expectedBookShelf : expectedStatesAfterPointer) {
            versionedBookShelf.redo();
            assertEquals(expectedBookShelf, new BookShelf(versionedBookShelf));
        }

        // check that there are no more states after pointer
        assertFalse(versionedBookShelf.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedBookShelf.undo());
    }

    /**
     * Creates and returns a {@code VersionedBookShelf} with the {@code bookShelfStates} added into it, and the
     * {@code VersionedBookShelf#currentStatePointer} at the end of list.
     */
    private VersionedBookShelf prepareBookShelfList(ReadOnlyBookShelf... bookShelfStates) {
        assertFalse(bookShelfStates.length == 0);

        VersionedBookShelf versionedBookShelf = new VersionedBookShelf(bookShelfStates[0]);
        for (int i = 1; i < bookShelfStates.length; i++) {
            versionedBookShelf.resetData(bookShelfStates[i]);
            versionedBookShelf.commit();
        }

        return versionedBookShelf;
    }

    /**
     * Shifts the {@code versionedBookShelf#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedBookShelf versionedBookShelf, int count) {
        for (int i = 0; i < count; i++) {
            versionedBookShelf.undo();
        }
    }

}
