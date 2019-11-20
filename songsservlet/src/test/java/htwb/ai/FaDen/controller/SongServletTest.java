package htwb.ai.FaDen.controller;

import htwb.ai.FaDen.dao.SongDao;
import htwb.ai.FaDen.model.InMemorySongs;
import htwb.ai.FaDen.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import javax.servlet.ServletException;
import javax.swing.text.html.parser.Entity;

import static org.junit.jupiter.api.Assertions.*;

class SongServletTest {

    private SongServlet servlet;
    private MockServletConfig config;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() throws ServletException {
        servlet = new SongServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        config = new MockServletConfig();
        config.addInitParameter("jsonDB", "testSongs.json");
        servlet.init(config); // throws ServletException
    }

    @Test
    void initUseCase() {
        assertNotNull(servlet.database);
        assertEquals(InMemorySongs.class, servlet.database.getClass());
        assertNotNull(servlet.dao);
        assertEquals(SongDao.class, servlet.dao.getClass());
        assertNotNull(servlet.filename);
        assertEquals("testSongs.json", servlet.filename);
    }


    @Test
    void doGetJsonUseCase() {
        try {
            request.setContextPath("/songsservlet-FaDen");
            request.setPathInfo("/songs");
            request.addParameter("songId", "2");
            request.addHeader("Accept", "application/json");

            servlet.doGet(request, response);

            assertEquals(200, response.getStatus());
            assertEquals("application/json", response.getContentType());
            assertTrue(response.getContentAsString().contains("{\"id\":2,\"title\":\"Mom\",\"artist\":\"Meghan Trainor, Kelli Trainor\",\"label\":\"Virgin\",\"released\":2016}"));
        } catch (Exception e) {
            fail("No exception should be thrown");
            e.printStackTrace();
        }
    }

    @Test
    void doGetNothingHeader() {
        try {
            request.setContextPath("/songsservlet-FaDen");
            request.setPathInfo("/songs");
            request.addParameter("songId", "2");

            servlet.doGet(request, response);

            assertEquals(200, response.getStatus());
            assertEquals("application/json", response.getContentType());
            assertTrue(response.getContentAsString().contains("{\"id\":2,\"title\":\"Mom\",\"artist\":\"Meghan Trainor, Kelli Trainor\",\"label\":\"Virgin\",\"released\":2016}"));
        } catch (Exception e) {
            fail("No exception should be thrown");
            e.printStackTrace();
        }
    }

    @Test
    void doGetUnsupportedHeaderType() {
        try {
            request.setContextPath("/songsservlet-FaDen");
            request.setPathInfo("/songs");
            request.addParameter("songId", "2");
            request.addHeader("Accept", "yolo/lit");

            servlet.doGet(request, response);

            assertEquals(406, response.getStatus());
        } catch (Exception e) {
            fail("No exception should be thrown");
            e.printStackTrace();
        }
    }

    @Test
    void doGetXmlUseCase() {
        try {
            request.setContextPath("/songsservlet-FaDen");
            request.setPathInfo("/songs");
            request.addParameter("songId", "2");
            request.addHeader("Accept", "application/xml");

            servlet.doGet(request, response);

            assertEquals(200, response.getStatus());
            assertEquals("application/xml", response.getContentType());
            assertTrue(response.getContentAsString().contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<song>\n" +
                    "    <artist>Meghan Trainor, Kelli Trainor</artist>\n" +
                    "    <label>Virgin</label>\n" +
                    "    <released>2016</released>\n" +
                    "    <title>Mom</title>\n" +
                    "</song>"));
        } catch (Exception e) {
            fail("No exception should be thrown");
            e.printStackTrace();
        }
    }

    @Test
    void doPutDifferentIDs() {
        try {
            String payload = "{\n" +
                    "    \"id\": 6,\n" +
                    "    \"title\": \"Wrecking Ball\",\n" +
                    "    \"artist\": \"MILEY CIRUS\",\n" +
                    "    \"label\": \"RCA\",\n" +
                    "    \"released\": 2013\n" +
                    "}";

            request.addHeader("Content-Type", "application/json");
            request.setContextPath("/songsservlet-FaDen");
            request.setPathInfo("/songs");
            request.addParameter("songId", "3");
            request.setContent(payload.getBytes());

            servlet.doPut(request, response);

            assertEquals("", response.getContentAsString());
            assertEquals(400,response.getStatus());
        } catch (Exception e) {
            fail("No exception should be thrown");
            e.printStackTrace();
        }
    }

    @Test
    void destroy() {
    }
}