package morghulis.valar.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class GenericDAOImpl<T extends Serializable> implements
		GenericDAO<T> {

	@PersistenceContext
	protected EntityManager em;

	private Class<T> type;

	@SuppressWarnings("unchecked")
	public GenericDAOImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@Override
	public Collection<T> getAllWiThNamedQuery(String namedQuery) {
		Collection<T> result = this.em.createNamedQuery(namedQuery, type)
				.getResultList();
		return result;
	}

	@Override
	public T findById(Long id) {
		return this.em.find(type, id);
	}

	@Override
	public void add(T item) {
		this.em.persist(item);
	}

	@Override
	public T makeQuery(TypedQuery<T> query) {
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
