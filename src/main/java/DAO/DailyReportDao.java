package DAO;

import model.DailyReport;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DailyReportDao {

    private Session session;

    public DailyReportDao(Session session) {
        this.session = session;
    }

    public List<DailyReport> getAllDailyReport() {
        Transaction transaction = session.beginTransaction();
        List<DailyReport> dailyReports = session.createQuery("FROM DailyReport").list();
        transaction.commit();
        session.close();
        return dailyReports;
    }

    public DailyReport getLast() {
        Transaction transaction = session.beginTransaction();
        DailyReport dailyReport = new DailyReport();
        List<Object[]> obj = session.createQuery("select earnings, soldCars from DailyReport where id = (select max(id) from DailyReport)").list();
        for (Object[] res : obj) {
            dailyReport.setEarnings((Long) res[0]);
            dailyReport.setSoldCars((Long) res[1]);
        }
        transaction.commit();
        session.close();
        return dailyReport;
    }

    public DailyReport addReport() {
        Transaction transaction = session.beginTransaction();
        DailyReport dailyReport = new DailyReport();
        List<Object[]> obj = session.createQuery("select sum(price) as earnings, count(sold) as soldCars from Car where sold = true and deleted = false").list();
        for (Object[] res : obj) {
            if (res[0] == null) {
                dailyReport.setEarnings(0L);
            } else {
                dailyReport.setEarnings((Long) res[0]);
            }
            dailyReport.setSoldCars((Long) res[1]);
        }
        session.save(dailyReport);
        transaction.commit();
        session.close();
        return dailyReport;
    }

    public boolean deleteAllReports() {
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery("delete from daily_reports where id > 0").executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }
}
