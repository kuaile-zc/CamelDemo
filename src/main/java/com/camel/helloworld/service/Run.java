package com.camel.helloworld.service;

import javax.xml.ws.Endpoint;

/**
 * @author CoreyChen Zhang
 * @date 2022/4/19 11:13
 * @modified
 */
public class Run {

    public static void main(String[] args) {
        String address = "http://0.0.0.0:8098/WebServiceDemo";
        Endpoint.publish(address, new WebServiceImpl());
        System.out.println("WebService Server 发布成功");
    }
}
