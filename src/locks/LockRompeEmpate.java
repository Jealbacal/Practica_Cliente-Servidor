package locks;

import java.util.Arrays;

public class LockRompeEmpate implements AnyLock {

    volatile int n;
    private volatile int [] in;
    private volatile int [] last;


    public LockRompeEmpate(int n){
        this.n=n;
        in=new int[n+1] ;
        last = new int[n+1];

        Arrays.fill(in,0);
        Arrays.fill(last,0);
        in=in;
        last=last;

    }


    public void takeLock(int i){

            for(int j=1;j<n+1;j++){

                in[i]=j;
                in=in;
                last[j]=i;
                last=last;

                for(int k=1; k<n+1 ;k++) {
                    if(k!=i)
                         while ((in[k]>=in[i]) && last[j]==i);
                }
            }


    }


    public void releaseLock(int i){
        in[i]=0;
        in=in;
    }

}
