package br.com.erudio.converter;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerParser {
	
	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObjectInputToObjectOutput(O  originalObject, Class<D> destinationObject) {
    	return mapper.map(originalObject, destinationObject);
    }

    public static <O, D> List<D> parserListObjectInputToObjectOutput(List<O> originalObjects, Class<D> destinationObject) {
        List<D> destinationObjects = new ArrayList<D>();
        for (Object originalObject : originalObjects) {
            destinationObjects.add(mapper.map(originalObject, destinationObject));
        }
        return destinationObjects;
    }

}
