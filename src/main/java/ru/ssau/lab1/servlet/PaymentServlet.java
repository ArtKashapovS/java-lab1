package ru.ssau.lab1.servlet;

import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.ssau.lab1.dao.AccountDAO;
import ru.ssau.lab1.dao.SeatDAO;
import ru.ssau.lab1.model.Account;
import ru.ssau.lab1.model.Seat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/pay")
public class PaymentServlet extends HttpServlet {

    @EJB
    private AccountDAO accountDAO;

    @EJB
    private SeatDAO seatDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        resp.setCharacterEncoding("UTF-8");
        List<Seat> seats = (List<Seat>) session.getAttribute("chosenSeats");
        String jsonOut = new Gson().toJson(seats);
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(jsonOut);
            writer.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = new Account();
        try (BufferedReader read = req.getReader()) {
            StringBuilder fullLine = new StringBuilder();
            String oneLine;
            while ((oneLine = read.readLine()) != null) {
                fullLine.append(oneLine);
            }
            JSONObject json = (JSONObject) new JSONParser().parse(fullLine.toString());

            String name = json.get("name").toString();
            String email = json.get("email").toString();
            acc.setName(name);
            acc.setEmail(email);
            accountDAO.create(acc);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        HttpSession session = req.getSession();
        List<Seat> seats = (List<Seat>) session.getAttribute("chosenSeats");
        Account createdAccount = accountDAO.findByID(acc.getId());
        for (Seat seat : seats) {
            seat.setAccount(createdAccount);
            seatDAO.update(seat);
        }

        resp.sendRedirect(req.getContextPath() + "/hall");
    }
}
