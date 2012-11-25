package org.openiam.dozer.crossentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dozer.ConfigurableCustomConverter;
import org.dozer.CustomConverter;
import org.dozer.DozerConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentSet;
import org.openiam.dozer.DozerDTOCorrespondence;
import org.openiam.util.SpringContextProvider;

public class CrossEntityPersistentListConverter extends AbstractCrossEntityCollectionConverter implements ConfigurableCustomConverter {
	
	@Override
	public Object convert(Object destVal, Object sourceVal, Class<?> destClass, Class<?> sourceClass) {
		List retVal = null;
		if(sourceVal != null) {
			final List sourceList = (List)sourceVal;
			retVal = new ArrayList(sourceList.size());
			convert(sourceList, retVal);
		}
		return retVal;
	}

	@Override
	public void setParameter(final String mapperBeanName) {
		if(StringUtils.isNotBlank(mapperBeanName)) {
			mapper = (Mapper)SpringContextProvider.getBean(mapperBeanName);
		}
	}
}
