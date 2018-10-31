package gridPlayer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class StateHandler {

    private StateObserver observer;

    public void setObserver(StateObserver observer){
        System.out.println("SETTING OBSERVER");
        this.observer = observer;
    }

    public boolean isObserverSet(){
        if(observer!=null){
            return true;
        } else {
            return false;
        }
    }

    public abstract Location getCurrentLoc();

    public abstract Location getCurrentGoal();

    public abstract Map<Location, List<Location>> getValidMoveList();

    public void updateNotify(){
        observer.runNotify();
    }

    public void updateSuccess() {observer.notifySuccess(); }

}
