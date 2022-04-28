package monitores;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorWR {
    private int nr;
    private int nw;
    private final Lock L = new ReentrantLock(true);
    private final Condition oktoRead = L.newCondition();
    private final Condition oktoWrite = L.newCondition();


    public MonitorWR(){
        this.nr=0;
        this.nw=0;
    }


    public void requestRead() throws InterruptedException {
        L.lock();
        while(nw>0) oktoRead.await();
        nr=nr+1;
        L.unlock();
    }

    public void releaseRead(){

        L.lock();

        nr=nr-1;
        if(nr==0) oktoWrite.signal();

        L.unlock();
    }

    public void requestWrite() throws InterruptedException {
        L.lock();

        while(nr>0 || nw >0) oktoWrite.await();

        nw++;
        L.unlock();
    }

    public void  releaseWrite(){

        L.lock();

        nw=nw-1;
        oktoWrite.signal();
        oktoRead.signal();
        L.unlock();
    }
}
