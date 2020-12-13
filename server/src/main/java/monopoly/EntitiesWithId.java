package monopoly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * Entities that are identifiable
 * 
 * @author Ziya Mukhtarov
 * @version Dec 12, 2020
 */
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
	
	/**
	 * Gives the object with a specified id
	 * 
	 * @param id the id to be searched with
	 * 
	 * @return T object if found, null if not
	 */
	public T getByID(int id) {
		return entities.get(id);
	}

	public int size() {
		return entities.size();
	}
}
