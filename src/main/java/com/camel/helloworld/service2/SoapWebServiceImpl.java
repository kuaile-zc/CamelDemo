package com.camel.helloworld.service2;

import javax.jws.WebService;

/**
 * @author CoreyChen Zhang
 * @date 2022/4/19 11:10
 * @modified
 */
@WebService
public class SoapWebServiceImpl implements SoapServiceInterface {

    @Override
    public Book getBook(String id) {
        System.out.println("get book :" + id);
        return new Book("1","Thinking in Java", "G.si");
    }
}
