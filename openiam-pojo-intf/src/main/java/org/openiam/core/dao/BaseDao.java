package org.openiam.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BaseDao<T, PrimaryKey extends Serializable> {

  T findById(PrimaryKey id);

  T findById(PrimaryKey id, String ... fetchFields);

  List<T> findAll();

  Long countAll();

  void save(T t);

  void delete(T t);

  void save(Collection<T> entities);

  void deleteAll()  throws Exception;
}
