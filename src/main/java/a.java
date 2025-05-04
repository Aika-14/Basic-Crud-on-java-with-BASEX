import org.basex.api.client.ClientSession;
import org.junit.jupiter.api.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class a {
    private static ClientSession session;
    private static final String DB_NAME = "TestDB";

    @BeforeAll
    static void setUp() throws IOException {
        session = new ClientSession("localhost", 1984, "admin", "admin");
        session.execute("CREATE DB " + DB_NAME + " <root/>");
        session.execute("OPEN " + DB_NAME);
    }

    @Test
    @Order(1)
    void testCreateRecord() throws IOException {
        String xml = "<book><title>Test Book</title></book>";
        session.execute("XQUERY insert node " + xml + " into /root");
        String result = session.execute("XQUERY /root/book/title/text()");
        assertEquals("Test Book", result.trim());
    }

    @Test
    @Order(2)
    void testReadRecord() throws IOException {
        String result = session.execute("XQUERY /root/book/title/text()");
        assertEquals("Test Book", result.trim());
    }

    @Test
    @Order(3)
    void testUpdateRecord() throws IOException {
        session.execute("XQUERY replace value of node /root/book/title with 'Updated Book'");
        String result = session.execute("XQUERY /root/book/title/text()");
        assertEquals("Updated Book", result.trim());
    }

    @Test
    @Order(4)
    void testDeleteRecord() throws IOException {
        session.execute("XQUERY delete node /root/book");
        String result = session.execute("XQUERY /root/book");
        assertEquals("", result.trim());
    }

    @AfterAll
    static void tearDown() throws IOException {
        session.execute("DROP DB " + DB_NAME);
        session.close();
    }
}