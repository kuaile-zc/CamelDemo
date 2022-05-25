package com.camel.helloworld.soapService;

import org.apache.log4j.Logger;

import javax.xml.ws.Endpoint;

public class QueryServiceServer {

	public void internalQueryService() throws Exception {

		String address = "http://localhost:9010/server/queryservice";

		Endpoint.publish(address, new QueryServicePortImpl());

		System.out.println("Camel 内部 WebService 发布成功 , address : " + address);

	}

}
