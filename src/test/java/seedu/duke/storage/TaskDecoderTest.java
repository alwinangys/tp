package seedu.duke.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.exceptions.DukeException;
import seedu.duke.items.Event;
import seedu.duke.items.Task;
import seedu.duke.items.characteristics.Member;
import seedu.duke.parser.Parser;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.duke.Duke.eventCatalog;
import static seedu.duke.Duke.memberRoster;

class TaskDecoderTest {

    @BeforeEach
    public void initializeTestEnvironment() throws DukeException {
        setUp();
    }

    @AfterEach
    public void reset() {
        eventCatalog.clear();
        memberRoster.clear();
    }

    @Test
    void decodeTaskFromString_stringValidFormat_expectOneTask() throws DukeException {
        String encodedTask = "t | Hype myself up | X | Drink lots of sugar | 19-02-2022 1950 | JOHN_DOE"
                + " | 0 | Peppa Pig's Concert";
        Task decodedTask = TaskDecoder.decodeTaskFromString(encodedTask);

        String expectedTitle = "Hype myself up";
        assertEquals(expectedTitle, decodedTask.getTitle());
        String expectedAssociatedEvent = "Peppa Pig's Concert";
        assertEquals(expectedAssociatedEvent, eventCatalog.get(0).getTitle());
    }

    @Test
    void decodeTaskFromString_outOfBoundsIndexOfEvent_exceptionThrown() {
        String encodedTask = "t | Hype myself up | X | Drink lots of sugar | 19-02-2022 1950 | JOHN_DOE"
                + " | 3 | Peppa Pig's Concert";
        assertThrows(DukeException.class, () -> {
            TaskDecoder.decodeTaskFromString(encodedTask);
        });
    }

    @Test
    void decodeTaskFromString_invalidMemberListSize_exceptionThrown() {
        String encodedTask = "t | Enter venue |   |  | 19-02-2022 1955 | BIGGEST_FAN | 1 | Peppa Pig's Concert";
        assertThrows(DukeException.class, () -> {
            TaskDecoder.decodeTaskFromString(encodedTask);
        });
    }

    /**
     * Generates an event catalog and member roster for testing.
     * Event 1 contains 2 sub-tasks, the first subtask contains all fields and 1 member whilst
     * the second contains an empty description field and 2 members.
     * Event 2 contains no sub-tasks.
     */
    private void setUp() throws DukeException {
        Member johnDoe = new Member("JOHN DOE");
        memberRoster.add(johnDoe);
        Member janeDoe = new Member("JANE DOE");
        memberRoster.add(janeDoe);

        LocalDateTime event1DateTime = Parser.convertDateTime("19-02-2022 2000");
        Event event1 = new Event("Peppa Pig's Concert",
                "Asia world tour", event1DateTime,
                "Indoor Stadium", 1000.90);

        LocalDateTime task1DateTime = Parser.convertDateTime("19-02-2022 1950");
        Task task1 = new Task("Hype myself up", "Drink lots of sugar", task1DateTime);
        task1.memberList.add(johnDoe);
        task1.markAsDone();
        task1.setEvent(event1);
        event1.addToTaskList(task1);
        event1.getFromTaskList(0).markAsDone();

        LocalDateTime task2DateTime = Parser.convertDateTime("19-02-2022 1955");
        Task task2 = new Task("Enter venue", "", task2DateTime);
        task2.memberList.add(johnDoe);
        task2.memberList.add(janeDoe);
        task2.setEvent(event1);
        event1.addToTaskList(task2);
        event1.markAsDone();

        LocalDateTime event2DateTime = Parser.convertDateTime("20-02-2022 2030");
        Event event2 = new Event("Funfair",
                "For charity", event2DateTime,
                "Parade square", 2000.10);
        ArrayList<Event> eventsList = new ArrayList<>();
        eventsList.add(event1);
        eventsList.add(event2);

        eventCatalog.addAll(eventsList);
    }
}
