package monopoly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EntitiesWithId<T extends Identifiable> {
	private Map<Integer, T> entities;

	public EntitiesWithId() {
		entities = Collections.synchronizedMap(new HashMap<Integer, T>());
	}

	public void add(T entity) {
		entities.put(entity.getId(), entity);
	}

	public void remove(T entity) {
		entities.remove(entity.getId());
	}

	public T getByID(int id) {
		return entities.get(id);
	}

	public int size() {
		return entities.size();
	}
}
