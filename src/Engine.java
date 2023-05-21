import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Engine {
    static int time=0;
    static Kernel kernel;
    static InterpreterEngine InterEng;
    static Scheduler scheduler;
    static Mutex mutex;
    static Memory memory;
    public static void main(String[] args) throws  Exception{

        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the desired Time Slice!!");
        int timeSlice=2;
        System.out.println("Enter Arrival Time of Process 1");
        int time1=0;
        System.out.println("Enter Arrival Time of Process 2");
        int time2=1;
        System.out.println("Enter Arrival Time of Process 3");
        int time3=3;
        memory=new Memory();
        kernel=new Kernel(timeSlice,memory);
        scheduler=new Scheduler(kernel);
        mutex=new Mutex(kernel,scheduler);
        InterEng=new InterpreterEngine(kernel,scheduler,mutex,memory);



        while (false&&!kernel.terminated&&time<8){
            System.out.println(" at Time="+time);
            System.out.println("---------------------------------------------------------------");
            if(time==time1)
                    kernel.addProc("Program_1");
            if(time==time2)
                    kernel.addProc("Program_2");
            if(time==time3)
                    kernel.addProc("Program_3");

            time++;
            if(!kernel.ready.isEmpty()||kernel.running!=null)
                InterEng.execute();
            System.out.println(Arrays.toString(memory.memo));
            System.out.println("---------------------------------------------------------------");
        }
    }
}
