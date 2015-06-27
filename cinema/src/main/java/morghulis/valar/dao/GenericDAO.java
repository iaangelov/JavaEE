package morghulis.valar.dao;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.TypedQuery;

import morghulis.valar.model.User;

public interface GenericDAO<T extends Serializable> {
	Collection<T> getAllWiThNamedQuery(String namedQuery);
	T findById(Long id);
	void add(T item);
	T makeQuery(TypedQuery<T> query);
}
