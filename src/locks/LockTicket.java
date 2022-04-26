package locks;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements AnyLock {
	volatile int[] turn;
	volatile AtomicInteger number;
	volatile int next;
	
	public LockTicket(int M) {
		turn = new int[M + 1];
		Arrays.fill(turn, 0);
		turn = turn;
		number = new AtomicInteger(1);
		next = 1;
	}
	
	public void takeLock(int threadID) {
		turn[threadID] = number.getAndAdd(1);
		turn = turn;
		while (turn[threadID] != next);
	}
	
	public void releaseLock(int threadID) { next = next + 1; }
}
