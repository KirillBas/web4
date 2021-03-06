package DAO;

import model.Car;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CarDao {

    private Session session;

    public CarDao(Session session) {
        this.session = session;
    }

    public List<Car> getAllCar() {
        Transaction transaction = session.beginTransaction();
        List<Car> result = session.createQuery("From Car where sold = false and deleted = false").list();
        transaction.commit();
        session.close();
        return result;
    }

    private Car getCarForModel(Car car) {
        List<Car> cars = session.createQuery("From Car where sold = false and deleted = false").list();
        for (int i = 0; i < cars.size(); i++) {
            Car carTemp = cars.get(i);
            if (carTemp.getBrand().equals(car.getBrand())
                && carTemp.getModel().equals(car.getModel())
                && carTemp.getLicensePlate().equals(car.getLicensePlate())) {
                return carTemp;
            }
        }
        return null;
    }

    public void setDeleteCars() {
        Transaction transaction = session.beginTransaction();
        List<Car> cars = session.createQuery("From Car where sold = true and deleted = false").list();
        for (Car carTemp : cars) {
            carTemp.setDeleted(true);
        }
        transaction.commit();
        session.close();
    }

    private boolean validateCountModel(String model) {
        Query query = session.createQuery("select count(*) from Car C where C.brand = :brand and sold = false and deleted = false").setParameter("brand", model);
        long count = (long) query.uniqueResult();
        return count < 10;
    }

    private boolean validateCar(Car car) {
        List<Car> cars = session.createQuery("From Car where sold = false and deleted = false").list();
        for (int i = 0; i < cars.size(); i++) {
            Car carTemp = cars.get(i);
            if (carTemp.getBrand().equals(car.getBrand())
                && carTemp.getModel().equals(car.getModel())
                && carTemp.getLicensePlate().equals(car.getLicensePlate())) {
                return true;
            }
        }
        return false;
    }

    public boolean addCar(Car car) {
        Transaction transaction = session.beginTransaction();
        if (validateCountModel(car.getModel())) {
            session.save(car);
            transaction.commit();
            session.close();
            return true;
        }
        transaction.commit();
        session.close();
        return false;
    }

    public void deleteCar(Car car) {
        Transaction transaction = session.beginTransaction();
        session.delete(car);
        transaction.commit();
        session.close();
    }

    public boolean sellCar(Car car) {
        Transaction transaction = session.beginTransaction();
        if (validateCar(car)) {
            Car soldCar = getCarForModel(car);
            soldCar.setSold(true);
            transaction.commit();
            session.close();
            return true;
        }
        transaction.commit();
        session.close();
        return false;
    }

    public boolean deleteAllCars() {
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery("delete from cars where id > 0").executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }
}
