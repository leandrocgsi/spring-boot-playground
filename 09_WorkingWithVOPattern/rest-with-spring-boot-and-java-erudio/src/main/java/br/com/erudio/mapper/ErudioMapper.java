package br.com.erudio.mapper;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class ErudioMapper {
	
	private static final MapperFactory mapper = new DefaultMapperFactory.Builder().build();
	
	public static <O, D> D parseObject(O origin, Class<D> destination) {
		return mapper.getMapperFacade().map(origin, destination);
	}
	
	public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
		List<D> destinationObjects = new ArrayList<D>();
		for (O o : origin) {
			destinationObjects.add(mapper.getMapperFacade().map(o, destination));
		}
		return destinationObjects;
	}

}
