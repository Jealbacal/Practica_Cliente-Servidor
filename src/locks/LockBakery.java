package locks;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class LockBakery implements AnyLock{

    volatile  int n;
    volatile  int turn[];

    public  LockBakery(int n){
        this.n=n;
        turn = new int[n+1];

    }
    @Override
    public void takeLock(int threadID) {
            turn[threadID]=1;
            turn=turn;
            IntStream stream = Arrays.stream(turn);
            OptionalInt opmayor= stream.max();
            int mayor= opmayor.getAsInt();
            turn[threadID]=mayor+1;
            turn= turn;

            for(int j=1;j<n+1;j++){
                if(j!=threadID)
                    while(turn[j]!=0 && operator(turn[threadID],threadID,turn[j],j));
            }

    }

    @Override
    public void releaseLock(int threadID) {
            turn[threadID]=0;
            turn=turn;
    }

    private boolean operator(int turn1, int threadID1, int turn2, int threadID2){

        return (turn1> turn2 ) || (turn1==turn2 && threadID1>threadID2);
    }
}
