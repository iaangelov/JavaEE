package morghulis.valar.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class GenericDAOImpl<T extends Serializable> implements
		GenericDAO<T> {

	@PersistenceContext
	protected EntityManager em;

	private Class<T> type;

	@SuppressWarnings("unchecked")
	protected GenericDAOImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@Override
	public T getSingleResultWithNamedQuery(String namedQuery) {
		return getSignleResultWithNamedQuery(namedQuery, null);
	}

	@Override
	public T getSignleResultWithNamedQuery(String namedQuery,
			Map<String, ?> parameters) {
		TypedQuery<T> query = em.createNamedQuery(namedQuery, type);
		if (parameters != null) {
			for (Map.Entry<String, ?> e : parameters.entrySet()) {
				query.setParameter(e.getKey(), e.getValue());
			}
		}
		return makeQuery(query);
	}

	@Override
	public Collection<T> getListWithNamedQuery(String namedQuery) {
		return getListWithNamedQuery(namedQuery, null);
	}

	@Override
	public Collection<T> getListWithNamedQuery(String namedQuery,
			Map<String, ?> parameters) {
		TypedQuery<T> query = this.em.createNamedQuery(namedQuery, type);
		if (parameters != null) {
			for (Map.Entry<String, ?> e : parameters.entrySet()) {
				String key = e.getKey();
				Object value = e.getValue();
				query.setParameter(key, value);
			}
		}
		Collection<T> result = query.getResultList();
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
	public void remove(T item) {
		if (item != null) {
			em.remove(item);
		}
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
