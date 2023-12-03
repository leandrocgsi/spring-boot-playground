package br.com.erudio.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DozerMapper {
    
    @Autowired
    ModelMapper mapper;
    
    public <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }
    
    public <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<D>();
        for (O o : origin) {
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }

}
