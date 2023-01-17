package ru.ssau.lab1.servlet;

import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.ssau.lab1.dao.SeatDAO;
import ru.ssau.lab1.services.AsyncTasksService;
import ru.ssau.lab1.model.Seat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(urlPatterns = "/hall", asyncSupported = true)
public class HallServlet extends HttpServlet {
    @EJB
    private SeatDAO seatDAO;
    private final AsyncTasksService asyncTasksService = AsyncTasksService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("handle");
        String action = req.getParameter("action");
        if (action.equals("new")) {
            resp.setCharacterEncoding("UTF-8");
            Collection<Seat> allSeats = seatDAO.getAllSeats();
            String jsonOut = new Gson().toJson(allSeats);
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(jsonOut);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (action.equals("update")) {
            asyncTasksService.addContext(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader read = req.getReader()) {
            StringBuilder fullLine = new StringBuilder();
            String oneLine;
            while ((oneLine = read.readLine()) != null) {
                fullLine.append(oneLine);
            }
            JSONArray json = (JSONArray) new JSONParser().parse(fullLine.toString());

            HttpSession session = req.getSession();
            synchronized (session) {
                List<Seat> seats = new ArrayList<>();
                for (Object o : json) {
                    JSONObject j = (JSONObject) o;
                    int sId = Integer.parseInt(j.get("id").toString());
                    Seat seat = seatDAO.findById(sId);
                    seats.add(seat);
                }
                session.setAttribute("chosenSeats", seats);

                String jsonOut = new Gson().toJson(seats);
                asyncTasksService.completeContexts(jsonOut);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
