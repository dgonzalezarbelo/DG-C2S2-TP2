package simulator.model;

public interface Observable<T> {
	void addObsever(T o);
	void removeObserver(T o);
}
