package htwb.ai.FaDen.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.FaDen.dao.SongDao;
import htwb.ai.FaDen.model.InMemorySongs;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class SongServlet extends HttpServlet {

    private InMemorySongs database;
    private SongDao dao;
    private String filename;

    @Override
    public void init(ServletConfig config) throws ServletException {
        filename = config.getInitParameter("jsonDB");
        this.dao = new SongDao();
        try {
            database = new InMemorySongs(dao.readJSONToSongs(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String pathInfo = request.getPathInfo();

        if(pathInfo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        StringTokenizer tokenizer = new StringTokenizer(pathInfo, "?");
        if(tokenizer.nextElement().equals("/songs")) {
            response.setContentType("application/json");
            try (PrintWriter out = response.getWriter()) {
                Enumeration<String> paramNames = request.getParameterNames();
                if(paramNames.hasMoreElements()) {
                    String parameter = paramNames.nextElement();
                    if(parameter.equals("songId")) {
                        String number = request.getParameter(parameter);
                        int index = Integer.parseInt(number);
                        objectMapper.writeValue(out, database.getSongs().get(index));
                    }
                }
            }
        }


//        response.setContentType("application/json");
//        try (PrintWriter out = response.getWriter()) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.writeValue(out, InMemorySongs.getInstance().getSongs());
//        }
    }

    @Override
    public void destroy() {
        try {
            dao.writeStoragetoJSON(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
