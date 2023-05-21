import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Process {
     PCB pcb;
     int curtoken;
     int runTime;
     Stack<Object> stack;


    public Process(int timeSlice,PCB p){
        pcb=p;
        runTime=timeSlice;
        curtoken=0;
        stack=new Stack<>();
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getRunTime() {
        return runTime;
    }

}
