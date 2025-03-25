package com.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.example.mapper.resultMapmapper;
import com.example.pojo.Route;
import com.example.pojo.resultMap;
import com.example.service.resultMapService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.*;
import java.net.URL;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.PSource;
import javax.sql.DataSource;
import java.util.logging.Level;
//服务实现类
@Primary
@Service
public class resultMapServiceImpl implements resultMapService {
    private static final double EARTH_RADIUS = 6371.0; // 地球半径
    //百度地图AK
    private final String ak = "vS0nrgdgw57rHHo9UBerl48uODQEX3bZ";
    //记录日志
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(resultMapServiceImpl.class.getName());
    //注入
    @Autowired
    private resultMapmapper Mapmapper;

    public List<resultMap>findAll(){
        return Mapmapper.findAll();
    }

    @Override
    public List<String> search(String name) {
        try {
            String urlStr = "https://www.baidu.com/s?wd="+name;
            System.out.println(urlStr);
//            URL url = new URL(urlStr);
//            URLConnection connection = url.openConnection();
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
//            StringBuilder content = new StringBuilder();
//            while ((inputLine = in.readLine()) !=null) {}
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void insert(resultMap map) {
        map.setName("起点");
        map.setId(1);
        if(map.getDescription()==null){map.setDescription("该景点暂无描述");}
        Mapmapper.insert(map);
    }

    @Override
    public void delete(Integer id) {
        Mapmapper.delete(id);
    }


    public resultMap find(Integer id) {
        return Mapmapper.find(id);
    }
    @Override
    public void deleteAll() {
        Mapmapper.deleteAll();
    }
    @Override
    public List<resultMap>start(resultMap Map) {
        //补全数据
       String map=Map.getName();
        List<resultMap> resultList = new ArrayList<>();
        List<resultMap> list1=getMap(map,resultList);
        List<resultMap> list=getDistance(list1);
        int i=2;
        for(resultMap ap:list){
            if(ap.getDescription()==null){ap.setDescription("该景点暂无描述");}
            ap.setId(i);
            i++;
            Mapmapper.insert(ap);}
        return Mapmapper.findAll();
    }
    //获取附近景点实现方法
    public List<resultMap> getMap(String address,List<resultMap>resultlist) {


        String urlStr = "https://api.map.baidu.com/place/v2/search?query=景点&tag=景点&region="+address+"&output=json&scope=1&ak="+ak;
        List<resultMap> resultList = resultlist;
        //获取坐标
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            String result = content.toString();
            // JSON字符串转JSON对象
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!jsonObject.containsKey("results") || jsonObject.getJSONArray("results") == null) {
                LOGGER.warning("Invalid response from API: " + result);
               return new ArrayList<>(); // 返回空列表
           }
            // 转换成数组
            JSONArray id = jsonObject.getJSONArray("results");


            for (int i = 0; i < id.size(); i++) {
                JSONObject poi = id.getJSONObject(i);
                JSONObject location = poi.getJSONObject("location");
                String name = poi.getString("name");
                String lng = location.getString("lng");
                String lat = location.getString("lat");
                resultMap poiInfo = new resultMap();
                poiInfo.setName(name);
                poiInfo.setLon(lng);
                poiInfo.setLat(lat);
                poiInfo.setImg(null);
                poiInfo.setDistance(0);
                resultList.add(poiInfo);
            }
            return resultList;

    } catch (MalformedURLException e) {
        LOGGER.log(Level.SEVERE, "Malformed URL: " + e.getMessage(), e);
    } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "IO Exception: " + e.getMessage(), e);
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
    }
        return new ArrayList<>(); // 返回空列表作为默认值
    }
    //获取路径点距离
     public List<resultMap> getDistance(List<resultMap>list) {
        double distance=0;
        List<resultMap> list2=new ArrayList<>();
        ArrayList<Double> list1=new ArrayList<>();
        int c=list.size()-1;
        for(int i=0;i<c;i++) {
            double lat1=0;
            double lon1=0;
            if(i==0) {
                lat1 = toRadians(Double.parseDouble(list.get(i).getLat()));
                lon1 = toRadians(Double.parseDouble(list.get(i).getLon()));
            }
            else {
                lat1 = toRadians(Double.parseDouble(list2.get(list2.size() - 1).getLat()));
                lon1 = toRadians(Double.parseDouble(list2.get(list2.size() - 1).getLon()));
            }
            list1.add(0.0);
            list2.add(null);
            for(int j=1;j<list.size();j++) {
                double lat2 = toRadians(Double.parseDouble(list.get(j).getLat()));
                double lon2 = toRadians(Double.parseDouble(list.get(j).getLon()));
                double dLat = toRadians(lat2 - lat1);
                double dLon = toRadians(lon2 - lon1);
                // 应用Haversine公式
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(lat1) * Math.cos(lat2) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
                distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)))*EARTH_RADIUS;
                if(!list1.isEmpty()&&(distance<list1.get(list1.size()-1)||list1.get(list1.size()-1)==0)){
                    list1.set(list1.size()-1,distance);
                    list2.set(list2.size()-1,list.get(j));
                }
            }list.remove(list2.get(list2.size()-1));

        }
            return list2;
}
    //坐标转换纬度
    private static double toRadians(double degree) {
        return degree * Math.PI / 180.0;
    }

    //实现路线规划
    public Map<String, Route> getRoute() {
        resultMap map1= Mapmapper.find(1);
        resultMap map2= Mapmapper.getLast();
        String str="";
        for(int i=1;i<=Mapmapper.findAll().size()-1;i++){
            resultMap map=Mapmapper.find(i);
            String lat=map.getLat();
            String lon=map.getLon();
            if(i==Mapmapper.findAll().size()-1){
                str+=lat+","+lon;
                break;
            }
            str+=lat+","+lon+"|";
        }
        System.out.println(str);
        String urlStr="https://api.map.baidu.com/direction/v2/driving?origin="+map1.getLat()+","+map1.getLon()+"&destination="+map2.getLat()+","+map2.getLon()+"&waypoints="+str+"&ak="+ak;
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            String results = content.toString();
            if (results != null && !"".equals(results)) {
                Map map = JSON.parseObject(results, Map.class);
                if ("0".equals(map.getOrDefault("status", "500").toString())) {
                    Map childMap = (Map) map.get("result");
                    JSONArray jsonArray = (JSONArray) childMap.get("routes");
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    JSONArray stepsArray=(JSONArray)(jsonObject.get("steps") == null ? "0" : jsonObject.get("steps"));

                    Map<String, Route> step = new HashMap<>();
                    for (int i = 0; i < stepsArray.size(); i++) {
                        String po =  stepsArray.getJSONObject(i).toString();
                        JSONObject poi = JSONObject.parseObject(po);
                        String roadName = poi.getString("road_name");
                        JSONObject startLocation = poi.getJSONObject("start_location");
                        String Distance = poi.getString("distance");
                        JSONObject endLocation = poi.getJSONObject("end_location");
                        String startLon = startLocation.getString("lng");
                        String startLat = startLocation.getString("lat");
                        String endLon = endLocation.getString("lng");
                        String endLat = endLocation.getString("lat");
                        Route route = new Route();
                        route.setStartLon(startLon);
                        route.setEndLat(endLat);
                        route.setEndLon(endLon);
                        route.setStartLat(startLat);
                        route.setdistance(Distance);
                        step.put(roadName,route);

                    }
                    return step;
                    }}}catch(MalformedURLException e){
                LOGGER.log(Level.SEVERE, "Malformed URL: " + e.getMessage(), e);
            } catch(IOException e){
                LOGGER.log(Level.SEVERE, "IO Exception: " + e.getMessage(), e);
            } catch(Exception e){
                LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            }
        return new HashMap<>();}

//    @Configuration
//    public class MyBatisConfig {
//
//        @Bean
//        public WebMvcConfigurer sqlSessionFactory(DataSource dataSource) throws Exception {
//
//            return new WebMvcConfigurer() {
//                @Override
//                public void addCorsMappings(CorsRegistry registry) {
//                    registry.addMapping("/api/**")
//                            .allowedOrigins("https://servicewechat.com")
//                            .allowedMethods("GET","POST","PUT","DELETE")
//                            .allowedHeaders("Content-Type");
//                }
//                };
//        }
//
//
//    }


}

