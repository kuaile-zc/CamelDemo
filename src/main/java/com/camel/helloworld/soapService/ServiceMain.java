package com.camel.helloworld.soapService;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author CoreyChen Zhang
 * @date 2022/4/1 17:02
 * @modified
 */
public class ServiceMain {


    public static void main(String[] args) throws Exception {

        QueryServiceServer queryServiceServer = new QueryServiceServer();
        queryServiceServer.internalQueryService();
    }
}
