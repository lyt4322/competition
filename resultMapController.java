package com.example.controller;

import com.example.pojo.Result;
import com.example.pojo.Route;
import com.example.pojo.resultMap;
import com.example.service.resultMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// 控制层
@Slf4j
@RestController
public class resultMapController {
    @Autowired
    private resultMapService service;

    //获取路线
    @RequestMapping(value = "/ads",method = RequestMethod.GET)
    public Result returnresult(){
        Map<String,Route>route=service.getRoute();
        return Result.success(route);
    }
//    @GetMapping(value = "/ads")
//    public Result getIntroduction(@RequestParam String name){
//        List<String> introduction=service.search(name);
//        return Result.success(introduction);
//    }


    //设置目的地返回附近景点
    @PostMapping(value ="/ads")
    public Result getPalace(@RequestParam String name,@RequestParam String lon,@RequestParam String lat){
        resultMap Map1=new resultMap();
        Map1.setLat(lat);
        Map1.setLon(lon);
        resultMap Map2=new resultMap();
        Map2.setName(name);
        service.deleteAll();
        service.insert(Map1);
        List<resultMap>list=service.start(Map2);
        return Result.success(list);
    }

    @DeleteMapping(value = "/ads")
    public Result delete(@RequestBody Integer id){
        service.delete(id);
        return Result.success();
    }


}
