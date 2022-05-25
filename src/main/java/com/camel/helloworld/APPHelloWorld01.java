package com.camel.helloworld;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;
import org.apache.camel.support.SimpleRegistry;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 郑重其事的写下 helloworld for Apache Camel
 */
public class APPHelloWorld01 extends RouteBuilder {

    /**
     * 为什么我们先启动一个Camel服务 再使用addRoutes添加编排好的路由呢？
     * 这是为很了告诉各位读者，Apache Camel支持动态加载/卸载编排的路由 这重要，因为后续设计的Broker需要依赖这种能力
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        final String url = "jdbc:mysql://10.255.249.3:3306/test";

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("123456");
        basicDataSource.setUrl(url);

        // 这是camel上下文对象，整个路由的驱动全靠它了。
        ModelCamelContext camelContext = new DefaultCamelContext();
        // 启动route
        camelContext.start();
        // 将我们编排的一个完整消息路由过程，加入到上下文中
        camelContext.addRoutes(new APPHelloWorld01());
        camelContext.getRegistry().bind("DataSource03", basicDataSource);



        // 为了保证主线程不退出
        synchronized (APPHelloWorld01.class) {
            APPHelloWorld01.class.wait();
        }
    }

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("jetty")
//                .bindingMode(RestBindingMode.json)
                // required because Camel needs to know which component
                // will be used to *invoke* REST services
                .producerComponent("http")
                .host("localhost").port("8099");

//        rest("/customers")
//                .get().to("direct:get");

        rest("/soap")
                .post().to("direct:soap");

        rest("/sql")
                .get().to("direct:sql");
//
//        rest("/multicast")
//                .get().to("direct:multicast");
//
//        rest("/serial")
//                .get().to("direct:serial");

        // TO http test
//        from("direct:soap")
//                .to("cxf:" +
//                        "http://10.255.249.22:8098/soap/userManage" +
//                        "?" +
//                        "wsdlURL=http://10.255.249.22:8098/soap/userManage?wsdl" +
//                        "&" +
//                        "dataFormat=RAW" +
////                        "&" +
////                        "portName=" +
//                        "&" +
//                        "skipPayloadMessagePartCheck=true"
//                )
//                .log("Response from the Get User operation was: ${body}");
//        from("direct:soap")
//                .to("cxf:http://localhost:8098/WebServiceDemo?wsdlURL=http://localhost:8098/WebServiceDemo?wsdl&dataFormat=RAW&skipPayloadMessagePartCheck=true")
//                .log("Response from the Get User operation was: ${body}");
//
//        // To SOAP test
//        Endpoint cxfEndpoint = endpoint("http://127.0.0.1:8091/soapWS/?wsdl" +
//                "?" +
//                "wsdlURL=http://127.0.0.1:8091/soapWS/?wsdl" +
//                "httpMethod=POST" +
//                "&dataFormat=RAW");

        String cxfUrl = "cxf:" +
                "http://localhost:8082/WebServiceDemo/sayHello" +
                "?" +
                "wsdlURL=http://localhost:8082/WebServiceDemo/sayHello?wsdl" +
                "&" +
                "dataFormat=PAYLOAD" +
                "&" +
                "skipPayloadMessagePartCheck=true";
//        CxfEndpoint cxfEndpoint1 = new CxfEndpoint(cxfUrl);

//        ((CxfEndpoint)cxfEndpoint).getFeatures().add(new LoggingFeature());

        String body = "1";


        from("direct:soap")
//                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(body);
                        System.out.println("");
                    }
                })
                .to(cxfUrl)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println();
                    }
                })
                .log("Response from the Get User operation was: ${body}");

        // TO sql test
        from("direct:sql")
                .setBody(constant("select * from user where id = 1"))
                .to("jdbc:DataSource03?outputType=SelectList").process(new Processor() {

            @Override
            public void process(Exchange exchange) throws Exception {

                System.out.println(exchange.toString());

                String str = exchange.getIn().getBody().toString();
                System.out.println("str : " + str);

                Object obj = exchange.getIn().getBody();
                System.out.println("obj : " + obj.getClass());
                System.out.println("obj : " + obj);

            }
        }).to("log:JDBCRoutesTest?showExchangeId=true");
//
//
//        // TO http aggregation
//        from("direct:multicast")
//                .multicast(new MyAggregationStrategy())
//                .parallelProcessing()
//                .removeHeaders("CamelHttp*")
//                .to("jetty:http://127.0.0.1:8080/sleep?" +
//                        "bridgeEndpoint=true"
//                )
//                .to("direct:get")
//                .log("Response from the Get User operation was: ${body}");
//
//        // TO http serial
//        from("direct:serial")
//                .to("http://127.0.0.1:8088/sleep2?httpMethod=GET&" +
//                                "bridgeEndpoint=true")
//                .to("direct:serial02")
//                .log("Response from the Get User operation was: ${body}");
//
//        from("direct:serial02")
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        String body = exchange.getIn().getBody(String.class);
//                        HashMap map =JSON.parseObject(body, HashMap.class);
//                        exchange.setProperty("camel-prefix-id", 9);
//                    }
//                })
//                .toD("http://127.0.0.1:8088/sleep3?httpMethod=GET&id=${property.camel-prefix-id}&bridgeEndpoint=true")
//                .log("Response from the Get User operation was: ${body}");



    }

    /**
     * 这个处理器用来完成输入的json格式的转换
     *
     * @author yinwenjie
     */
    public class HttpProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            // 因为很明确消息格式是http的，所以才使用这个类
            // 否则还是建议使用org.apache.camel.Message这个抽象接口
            System.out.println("Begin process!!!!");
            HttpMessage message = (HttpMessage) exchange.getIn();
            InputStream bodyStream = (InputStream) message.getBody();
            String inputContext = this.analysisMessage(bodyStream);
            bodyStream.close();

            // 存入到exchange的out区域
            if (exchange.getPattern() == ExchangePattern.InOut) {
                Message outMessage = exchange.getOut();
                outMessage.setBody(inputContext + " || out");
            }
        }

        /**
         * 从stream中分析字符串内容
         *
         * @param bodyStream
         * @return
         */
        private String analysisMessage(InputStream bodyStream) throws IOException {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] contextBytes = new byte[4096];
            int realLen;
            while ((realLen = bodyStream.read(contextBytes, 0, 4096)) != -1) {
                outStream.write(contextBytes, 0, realLen);
            }

            // 返回从Stream中读取的字串
            try {
                return new String(outStream.toByteArray(), "UTF-8");
            } finally {
                outStream.close();
            }
        }
    }

}
