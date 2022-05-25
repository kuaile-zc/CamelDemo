package com.camel.helloworld.start;

import com.camel.helloworld.APPHelloWorld01;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * @author CoreyChen Zhang
 * @date 2022/5/16 15:39
 * @describe TODO
 */
public class startDemo {

    public static void main(String[] args) throws Exception {
        DefaultCamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer://myTime?period=2000")
                        .setBody(simple("Current time is [ ${header.firedTime} ]"))
                        .to("stream:err");
            }
        });
        camelContext.start();

        // 为了保证主线程不退出
        synchronized (startDemo.class) {
            startDemo.class.wait();
        }
    }
}
