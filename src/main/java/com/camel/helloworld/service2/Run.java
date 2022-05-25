package com.camel.helloworld.service2;

import javax.xml.ws.Endpoint;

/**
 * @author CoreyChen Zhang
 * @date 2022/4/19 11:13
 * @modified
 */
public class Run {

    public static void main(String[] args) {
        String address = "http://localhost:8084/SoapServiceDemo";
        Endpoint.publish(address, new SoapWebServiceImpl());
        System.out.println("SoapService Server 发布成功");
    }
}
