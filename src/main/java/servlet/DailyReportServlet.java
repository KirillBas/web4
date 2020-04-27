package servlet;

import com.google.gson.Gson;
import service.CarService;
import service.DailyReportService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DailyReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo().contains("all")) {
            Gson gson = new Gson();
            String json = gson.toJson(DailyReportService.getInstance().getAllDailyReports());
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(json);
            out.flush();
        } else if (req.getPathInfo().contains("last")) {
            Gson gson = new Gson();
            String json = gson.toJson(DailyReportService.getInstance().getLastReport());
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(json);
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(CarService.getInstance().getDeleteAllCars() && DailyReportService.getInstance().getDeleteAllReports() ? HttpServletResponse.SC_OK : HttpServletResponse.SC_FORBIDDEN);
    }
}
