package com.example.service;

import com.example.pojo.Route;
import com.example.pojo.resultMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;

@Mapper
//服务接口
public interface resultMapService {
//    获取景点描述

    public List<String> search(String name);
    public void insert(resultMap map);
    //    删除景点
    public void delete(Integer id);
      //清空数据库数据
    public void deleteAll();
    //获取路线
    public Map<String, Route> getRoute();

    //获取目的地
    List<resultMap> start(resultMap map);
}

