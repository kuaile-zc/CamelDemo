package com.camel.helloworld.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author CoreyChen Zhang
 * @date 2022/3/25 15:37
 * @modified
 */
@WebService
public interface BaseicQuery {

    /**
     * 查询人员信息
     *
     * @param queryCondition 基础查询
     * @return
     */
    @WebMethod
    String queryPeopleInfo(@WebParam(name = "queryCondition") String queryCondition);
}
