package com.camel.helloworld.service;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CoreyChen Zhang
 * @date 2022/4/19 11:10
 * @modified
 */
@WebService
public class WebServiceImpl implements WebServiceInterface{

    @Override
    public Car getCar(String id) {
        System.out.println("Say hello!");
        Integer carId = Integer.valueOf(id);
        List<Car> list = new ArrayList<>();
        list.add(new Car("1","Ben", "320000"));
        list.add(new Car("2","Byd", "210000"));
        list.add(new Car("3","Honda", "160000"));
        return carId < list.size() ? list.get(carId) : list.get(0);
    }
}
