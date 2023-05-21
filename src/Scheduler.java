public class Scheduler {
    Kernel kernel;
    public Scheduler(Kernel k){
        kernel=k;
    }
    public void dispatch(){
        if(kernel.ready.isEmpty()){
            kernel.terminated=true;
            kernel.output("terminate");
            return;
        }
        Process run=kernel.ready.remove();
        kernel.running=run;
        run.pcb.procState=State.Running;

        kernel.running.setRunTime(kernel.curTimeSlice);

        kernel.output("dispatch");

    }
    public void preempt(){
        kernel.ready.add(kernel.running);
        kernel.output("preempt");
        dispatch();
    }
    public void finish() {
        kernel.output("finish");
        dispatch();
    }
}
