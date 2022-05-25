package com.camel.helloworld.soap;

/**
 * @author CoreyChen Zhang
 * @date 2022/3/25 15:38
 * @modified
 */
public class BaseicQueryImpl implements BaseicQuery {
    @Override
    public String queryPeopleInfo(String queryCondition) {
        return "info";
    }
}
