package com.camel.helloworld;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

/**
 * @author CoreyChen Zhang
 * @date 2022/3/28 10:57
 * @modified
 */
public class MyAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // the first time there are no existing message and therefore
        // the oldExchange is null. In these cases we just return
        // the newExchange
        if (oldExchange == null) {
            return newExchange;
        }

        // now we have both an existing message (oldExchange)
        // and a incoming message (newExchange)
        // we want to merge together.

        // in this example we add their bodies
        String oldBody = oldExchange.getIn().getBody(String.class).trim();
        String newBody = newExchange.getIn().getBody(String.class).trim();

        // the body should be the two bodies added together
        String body = oldBody + newBody;

        // update the existing message with the added body
        oldExchange.getIn().setBody(body);
        // and return it
        return oldExchange;
    }
}
