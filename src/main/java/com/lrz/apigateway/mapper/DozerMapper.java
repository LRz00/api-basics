/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lara
 */
public class DozerMapper {
    
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    
    public static<O, D> D parseObject(O origin, Class<D> destination){
        return mapper.map(origin, destination);
    }
    
     public static<O, D> List<D> parseListObjects(List<O> origin, Class<D> destination){
         List<D> destinationObjects = new ArrayList<D>();
         
         for(O o: origin){
             destinationObjects.add(mapper.map(o, destination));
         }
         return destinationObjects;
    }
}
    
