package panayotov.week1.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.persistence.TypedQuery;

public interface GenericDAO<T extends Serializable> {
	Collection<T> getListWithNamedQuery(String namedQuery);

	Collection<T> getListWithNamedQuery(String namedQuery,
			Map<String, ?> parameters);

	T getSingleResultWithNamedQuery(String namedQuery);

	T getSignleResultWithNamedQuery(String namedQuery, Map<String, ?> parameters);

	T findById(Long id);

	void add(T item);

	void remove(T item);

	T makeQuery(TypedQuery<T> query);
}
