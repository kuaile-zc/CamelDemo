package com.camel.helloworld.soap.test;

import com.camel.helloworld.APPHelloWorld01;
import com.camel.helloworld.soap.BaseicQueryProcess;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;

/**
 * @author CoreyChen Zhang
 * @date 2022/3/25 15:42
 * @modified
 */
public class SoapRun extends RouteBuilder {


    public static void main(String[] args) {

        try {

            CamelContext camelContext = new DefaultCamelContext();
            camelContext.start();
            camelContext.addRoutes(new SoapRun());

            System.out.println("baseic query service address : "  + "?wsdl");

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

        String soap = "cxf:" +
                "http://127.0.0.1:8083/WebServiceDemo" +
                "?" +
                "wsdlURL=conf/wsdl/test.wsdl" +
                "&" +
                "portName=WebServiceImplPort" +
                "&" +
                "dataFormat=RAW" +
                "&" +
                "skipPayloadMessagePartCheck=true";

        from(soap)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String body = exchange.toString();
                        String out = exchange.getOut().getBody(String.class);
                        System.out.println("This is body: ---" + body);
                        System.out.println("This is body: ---" + out);
                        exchange.getIn().setHeader("Content-Type", "application/json;charset=utf-8");
                    }
                })
                .to("jetty:http://127.0.0.1:8080/sleep" +
                            "?bridgeEndpoint=true" +
                            "@httpMethodRestrict=GET"
                    )
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String out = exchange.getOut().getBody(String.class);
                        exchange.getOut().setBody("{}");
                    }
                })
                .log("Response from the Get User operation was: ${body}");

    }

}
