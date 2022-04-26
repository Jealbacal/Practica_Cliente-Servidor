package locks;

public interface AnyLock {

    public void takeLock(int threadID);

    public void releaseLock(int threadID);

}
