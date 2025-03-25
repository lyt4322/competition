package com.example.mapper;

import com.example.pojo.resultMap;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
//数据层
@Mapper
public interface resultMapmapper {
    //    查询目地的和起点范围内的景点
    @Select("select id,name,description,distance,img,lat,lon from result_map order by distance desc ")
    public List<resultMap> findAll();
    @Select("select id,name,description,distance,img,lat,lon from result_map where id=#{id}")
    public resultMap find(Integer id);
    @Select("select id,name,description,distance,img,lat,lon from result_map order by id desc limit 1")
    public resultMap getLast();

    //创建表外加设置目的地
    @Insert("insert into result_map(id,name,description,distance,img,lat,lon) " +
            "values (#{id},#{name},#{description},#{distance},#{img},#{lat},#{lon})")
    void insert(resultMap map);

    // 删除表中的所有数据
    @Delete("delete from result_map")
    void deleteAll();
    @Delete("delete from result_map where id=#{id}")
    void delete(Integer id);
}
