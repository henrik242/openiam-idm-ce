package org.openiam.dozer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;
import org.dozer.Mapper;
import org.openiam.util.SpringContextProvider;

public class MapConverterWithKeyString implements ConfigurableCustomConverter {

	private Mapper mapper = null;
	
	@Override
	public Object convert(Object destVal, Object sourceVal, Class<?> destClass, Class<?> sourceClass) {
		Map userAttributes = null;
		if(sourceVal != null) {
			userAttributes = new HashMap();
			final Map sourceMap = (Map)sourceVal;
			for(final Object key : sourceMap.keySet()) {
				final Object value = sourceMap.get(key);
				if(value != null) {
					userAttributes.put(key, mapper.map(value, value.getClass()));
				}
			}
		}
		return userAttributes;
	}

	@Override
	public void setParameter(final String mapperBeanName) {
		if(StringUtils.isNotBlank(mapperBeanName)) {
			mapper = (Mapper)SpringContextProvider.getBean(mapperBeanName);
		}
	}
}
