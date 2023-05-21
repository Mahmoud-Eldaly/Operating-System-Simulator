import java.util.*;
public class InterpreterEngine{
    static Kernel kernel;
    HashSet<String>keywords;
    static String[] tokens;
    Scheduler scheduler;
    Mutex mutex;
    Memory memory;


    public InterpreterEngine(Kernel kernel,Scheduler s,Mutex m,Memory memory){
        this.memory=memory;
       this.kernel=kernel;
       this.scheduler=s;
       this.mutex=m;
       keywords=new HashSet<>();
        keywords.add("print");
        keywords.add("assign");
        keywords.add("writeFile");
        keywords.add("readFile");
        keywords.add("printFromTo");
        keywords.add("semWait");
        keywords.add("semSignal");
        keywords.add("input");
    }


    public void execute(){
        if(kernel.running==null) {
            kernel.output("begin");
            scheduler.dispatch();
        }
        kernel.running.setRunTime(kernel.running.getRunTime()-1);
        System.out.println(kernel.running.pcb.ProgramCounter+" "+kernel.running.pcb.MemBoundaries.start);
        System.out.println(Arrays.toString(memory.memo));
        String s=memory.memo[kernel.running.pcb.ProgramCounter+kernel.running.pcb.MemBoundaries.start];
        tokens=s.split(" ");
        if(kernel.running.curtoken==0){
            kernel.running.curtoken= tokens.length;
        }
        Process org=kernel.running;
        int i=kernel.running.curtoken-1;
        while(!keywords.contains(tokens[i]))
            org.stack.push(tokens[i--]);
        kernel.running.curtoken=i;

        System.out.println("Running process: "+kernel.getProcName(kernel.running));
        System.out.println("Running instruction: "+s);

        switch (tokens[i]){
            case "print":kernel.print(""+org.stack.pop()); break;
            case "assign": kernel.assign(""+org.stack.pop(),""+org.stack.pop());break;
            case "writeFile":kernel.writeFile(""+org.stack.pop(),""+org.stack.pop());break;
            case "readFile" :org.stack.push( kernel.readFile(""+org.stack.pop()));break;
            case "printFromTo":printFromTo((""+org.stack.pop()),(""+org.stack.pop()));break;
            case "semWait": mutex.semWait(""+org.stack.pop());break;
            case "semSignal":mutex.semSignal(""+org.stack.pop());break;
            case "input":org.stack.push(kernel.getInput());break;

        }

        if(org.curtoken==0)
            org.pcb.ProgramCounter++;
        //use boundaries here
        if(kernel.running.pcb.MemBoundaries.end-3<kernel.running.pcb.ProgramCounter)
            scheduler.finish();
        else if(kernel.running.getRunTime()==0)
            scheduler.preempt();


    }
    public void printFromTo(String a,String b){
        Integer x,y;
        try {
            x=Integer.parseInt(a);
            y=Integer.parseInt(b);
        }
        catch (Exception ex){
            int r1=0,r2=0;
            //a,b,c
            switch (a){
                case "a":r1=-2; break;
                case "b":r1=-1; break;
                case "c":r1=0; break;
            }
            switch (b){
                case "a":r2=-2; break;
                case "b":r2=-1; break;
                case "c":r2=0; break;
            }
            x=Integer.parseInt(this.memory.memo[kernel.running.pcb.MemBoundaries.end+r1]);
            y=Integer.parseInt(this.memory.memo[kernel.running.pcb.MemBoundaries.end+r2]);
        }

        while (x<=y)
            kernel.print((x++)+" ");
        System.out.println();
    }

}
