package htwb.ai.FaDen.controller;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.FaDen.dao.SongDao;
import htwb.ai.FaDen.model.Song;
import htwb.ai.FaDen.utils.ContentType;
import htwb.ai.FaDen.utils.ParameterException;
import htwb.ai.FaDen.model.InMemorySongs;
import htwb.ai.FaDen.utils.PayloadException;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SongServlet extends HttpServlet {

    public InMemorySongs database; //public wegen tests :(
    public SongDao dao;
    public String filename;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) {
        filename = config.getInitParameter("jsonDB");
        objectMapper = new ObjectMapper();
        this.dao = new SongDao();
        try {
            database = new InMemorySongs(dao.readJSONToSongs(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String checkUrlPath(HttpServletRequest request, HttpServletResponse response) throws ParameterException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.equals("/songs") || !request.getContextPath().equals("/songsservlet-FaDen")) {
            throw new ParameterException(HttpServletResponse.SC_BAD_REQUEST, "Not a valid path.");
        }
        return pathInfo; //TODO unneccasry
    }


    public int getSongId(HttpServletRequest request, HttpServletResponse response) throws ParameterException {
        String argument = request.getParameter("songId");
        if (argument == null) throw new ParameterException(HttpServletResponse.SC_BAD_REQUEST, "Not a valid parameter");

        return parseArgument(argument);
    }

    private int parseArgument(String number) throws ParameterException {
        int index = -1;
        try {
            index = Integer.parseInt(number);
        } catch (NumberFormatException ignored) {}

        if (!checkValid(index)) throw new ParameterException(HttpServletResponse.SC_NOT_FOUND, "Not a valid argument.");
        return index;
    }

    private boolean checkValid(int index) {
        return index >= 1 && index <= 10;
    }

    public void sendResponse(int index, ContentType contentType, HttpServletResponse response) {
        switch (contentType) {
            case JSON:
                sendJson(index, response);
                break;
            case XML:
                sendXml(index, response);
                break;
        }
    }

    private void sendJson(int index, HttpServletResponse response) {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            objectMapper.writeValue(out, database.get(index));
        } catch (IOException ignored) {}
    }

    private void sendXml(int index, HttpServletResponse response) {
        response.setContentType("application/xml");
        try (PrintWriter out = response.getWriter()) {
            out.write(dao.convertSongToXml(database.get(index)));
        } catch (IOException ignored) {}
    }

    private void updateJsonSong(int songId, HttpServletRequest request) throws ParameterException, PayloadException, IOException {
        Song song = database.get(songId);

        try (BufferedReader in = request.getReader()) {
            Song submittedSong = objectMapper.readValue(in, new TypeReference<Song>(){});
            if (!submittedSong.valid()) throw new PayloadException(400, "Given payload is not valid.");
            if (!(song.getId() == submittedSong.getId())) throw new ParameterException(400, "Query did not match with given payload.");
            database.update(songId, submittedSong);
        } catch (JsonParseException parse) {
            throw new PayloadException(400, "Could not parse JSON.");
        } catch (JsonMappingException map) {
            throw new PayloadException(400, "Could not map JSON to Object.");
        }
    }


    public ContentType getContentTypeResponse(HttpServletRequest request) throws ParameterException {
        String header = request.getHeader("Accept");

        if (header == null || header.isEmpty() || header.equals("*/*")) return ContentType.JSON;
        else if (header.contains("application/xml")) return ContentType.XML;
        else if (header.contains("application/json")) return ContentType.JSON;
        else throw new ParameterException(HttpServletResponse.SC_NOT_ACCEPTABLE, "Can't satisfy header"); //sollte eigentlich nie geworfen werden
    }

    public ContentType putContentTypeResponse(HttpServletRequest request) throws ParameterException {
        String header = request.getHeader("Content-Type");

        if (header.equals("application/json")) return ContentType.JSON;
        throw new ParameterException(HttpServletResponse.SC_BAD_REQUEST, "Unsupported Content-Type");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            checkUrlPath(request, response);
            ContentType contentType = getContentTypeResponse(request);
            int index = getSongId(request, response);
            sendResponse(index, contentType, response);
        } catch (ParameterException e) {
            response.sendError(e.getErrorCode(), e.getErrorMessage());
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            checkUrlPath(request, response);
            ContentType contentType = putContentTypeResponse(request);
            int index = getSongId(request, response);
            updateJsonSong(index, request);
            response.setStatus(204);
        } catch (PayloadException e) {
            response.sendError(e.getErrorCode(), e.getErrorMessage());
        } catch (ParameterException e) {
            response.sendError(e.getErrorCode(), e.getErrorMessage());
        }
    }

    @Override
    public void destroy() {
        try {
            dao.writeStoragetoJSON(database, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}