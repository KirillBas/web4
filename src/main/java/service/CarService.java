package service;

import DAO.CarDao;
import model.Car;
import org.hibernate.SessionFactory;
import util.DBHelper;

import java.util.List;

public class CarService {

    private static CarService carService;

    private SessionFactory sessionFactory;

    private CarService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static CarService getInstance() {
        if (carService == null) {
            carService = new CarService(DBHelper.getSessionFactory());
        }
        return carService;
    }

    public List<Car> getAllCars() {
        return new CarDao(sessionFactory.openSession()).getAllCar();
    }

    public boolean getAddCar(Car car) {
        return new CarDao(sessionFactory.openSession()).addCar(car);
    }

    public void getDeleteCar(Car car) {
        new CarDao(sessionFactory.openSession()).deleteCar(car);
    }

    public boolean getSellCar(Car car) {
        return new CarDao(sessionFactory.openSession()).sellCar(car);
    }

    public void getDeleteAllCars() {
        new CarDao(sessionFactory.openSession()).deleteAllCars();
    }
}
