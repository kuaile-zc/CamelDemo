package com.camel.helloworld.soap;

import com.camel.helloworld.APPHelloWorld01;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

/**
 * @author CoreyChen Zhang
 * @date 2022/3/25 15:42
 * @modified
 */
public class BaseicRun extends RouteBuilder {


    private static final String LOGBACK_FILENAME = "./conf/logback.xml";

    public static final String BASEICQUERY_SERVICE_URL = "http://localhost:9999/baseQueryService";

    public static final String  BASEQUERY_SERVICE_URL_ADDRESS = "cxf://" + BASEICQUERY_SERVICE_URL + "?serviceClass=com.camel.helloworld.soap.BaseicQuery";

    public static void main(String[] args) {

        try {

            CamelContext camelContext = new DefaultCamelContext();
            camelContext.start();
            camelContext.addRoutes(new BaseicRun());

            System.out.println("baseic query service address : " + BASEICQUERY_SERVICE_URL + "?wsdl");

            // 为了保证主线程不退出
            synchronized (APPHelloWorld01.class) {
                APPHelloWorld01.class.wait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void configure() throws Exception {

        from(BASEQUERY_SERVICE_URL_ADDRESS)
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String body = exchange.getOut().getBody(String.class);
                        String in = exchange.getIn().getBody(String.class);
//                        exchange.getOut().setBody("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
//                                "    <soap:Body>321</soap:Body>\n" +
//                                "</soap:Envelope>");
//                        exchange.getOut().setBody("321");
                    }
                })
                    .to("jetty:http://127.0.0.1:8080/sleep" +
                            "?bridgeEndpoint=true" +
                            "&httpMethodRestrict=GET"
                    )
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String body = exchange.getOut().getBody(String.class);
                        String in = exchange.getIn().getBody(String.class);
                    }
                })
//                .marshal().jaxb()
                .log("Response from the Get User operation was: ${body}");

    }

}
