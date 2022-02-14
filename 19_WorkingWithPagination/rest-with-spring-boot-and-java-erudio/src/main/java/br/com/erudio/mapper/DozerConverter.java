package br.com.erudio.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerConverter {
    
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    
    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }  
    
    public static <O, D> Page<D> parsePageOfObjects(Page<O> origin, Class<D> destination) {
        List<D> list = parseListObjects(origin.getContent(), destination);
        return new PageImpl<D>(list);
    }    
    
    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<D>();
        for (Object o : origin) {
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }
}
