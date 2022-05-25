package com.camel.helloworld.soap.test;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author CoreyChen Zhang
 * @date 2022/3/31 16:26
 * @modified
 */
@WebService
public interface QuoteInEndpoint {

    @WebResult(name = "quote")
    public Quote price(@WebParam(name = "symbol") String symbol);

}
