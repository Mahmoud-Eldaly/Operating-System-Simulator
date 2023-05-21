import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
    Kernel kernel;
    Scheduler scheduler;

    public Mutex(Kernel k,Scheduler s){
        kernel=k;
        scheduler=s;
    }
    public void semWait(String s){
        for (Triple t: kernel.resources) {
            if(t.resName.equals(s)){
                if(!t.available) {
                    kernel.blocked.add(kernel.running);
                    kernel.blockedOn.put(kernel.running,s);
                    kernel.output("block");
                    scheduler.dispatch();
                }
                else{
                    t.available=false;
                    t.lockProc=kernel.running.pcb.procID;
                }
            }
        }
    }
    public void semSignal(String s){

        for (Triple t: kernel.resources) {
            if(t.resName.equals(s)){
                if(t.lockProc== kernel.running.pcb.procID){
                        Process free=null;
                        Queue<Process> newBlocked=new LinkedList<>();
                        while (!kernel.blocked.isEmpty()){
                            Process cur=kernel.blocked.remove();
                            if(kernel.blockedOn.get(cur).equals(s)&&free==null) {
                                free = cur;
                                kernel.blockedOn.remove(cur);
                            }
                            else
                                newBlocked.add(cur);
                        }
                        kernel.blocked = newBlocked;
                        if(free!=null) {
                            kernel.ready.add(free);
                            t.lockProc = free.pcb.procID;
                        }
                        else{
                            t.available=true;
                            t.lockProc=-1;
                        }

                }
            }
        }
    }
}
