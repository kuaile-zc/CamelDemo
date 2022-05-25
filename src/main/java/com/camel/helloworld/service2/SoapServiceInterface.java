package com.camel.helloworld.service2;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author CoreyChen Zhang
 * @date 2022/4/19 11:09
 * @modified
 */
@WebService
public interface SoapServiceInterface {

    @WebMethod
    public Book getBook(String id);
}
