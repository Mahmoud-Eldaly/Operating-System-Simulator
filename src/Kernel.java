import java.io.*;
import java.util.*;

public class Kernel {
    Queue<Process> ready;
    Queue<Process> blocked;
    HashMap<Process,String> blockedOn;
    Process running;
    ArrayList<Triple> resources;
    boolean terminated;
    int curTimeSlice;
    Memory memory;
    static HashMap< Integer,String> NameID=new HashMap<>();
    int counterID=0;

   public Kernel(int curTimeSlice,Memory m){
       terminated=false;
       ready=new LinkedList<>();
       blocked=new LinkedList<>();
       resources=new ArrayList<>();
       blockedOn=new HashMap<>();
       this.curTimeSlice=curTimeSlice;

       memory=m;


       resources.add(new Triple("file",true,-1));
       resources.add(new Triple("userInput",true,-1));
       resources.add(new Triple("userOutput",true,-1));

   }

   public void addProc(String path) throws IOException {
       path = "F:\\OS_22_Project" + "\\" + path + ".txt";
       System.out.println("Adding NEW proc");
       BufferedReader reader = new BufferedReader(new FileReader(path));
       int lines = 0;
       while (reader.readLine() != null) lines++;
       reader.close();

       int start = 12;
       while (memory.valid[start] != null) start++;
       System.out.println("trying to loooad "+start+" "+lines);
       if (start + lines + 3 > 40 || memory.valid[start + lines + 3-1] != null) {
           System.out.println("iffff");
           unload();
           PCB pcb = new PCB(this.counterID++, 12, 12 + lines + 3-1);
           String tmp = "Process " + path.charAt(path.length() - 1);
           NameID.put(pcb.procID, tmp);
           int st = 0;
           while (memory.valid[st] != null) st++;
           memory.memo[st + 0] = pcb.procID + "";
           memory.memo[st + 1] = pcb.procState + "";
           memory.memo[st + 2] = pcb.ProgramCounter + "";
           memory.memo[st + 3] = pcb.MemBoundaries + "";
           memory.valid[st + 0] = NameID.get(pcb.procID);
           memory.valid[st + 1] = NameID.get(pcb.procID);
           memory.valid[st + 2] = NameID.get(pcb.procID);
           memory.valid[st + 3] = NameID.get(pcb.procID);

           for (int i = pcb.MemBoundaries.start; i <= pcb.MemBoundaries.end; i++) {
               memory.valid[i] = NameID.get(pcb.procID);
           }
           start = 12;
           try {
               File file = new File(path);
               Scanner input = new Scanner(file);


               while (input.hasNextLine()) {
                   String s = input.nextLine();
                   memory.memo[start] = s;
                   memory.valid[start] = NameID.get(pcb.procID);
                   start++;
               }
               for (int i = 0; i < 3; i++) {
                   memory.valid[start+i] = NameID.get(pcb.procID);
               }
               input.close();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
           ready.add(new Process(curTimeSlice,pcb));


       }
       else {
           System.out.println("elssse");
           start=12;
           while (memory.valid[start] != null) start++;
           PCB pcb = new PCB(this.counterID++, start, start + lines + 3-1);

           String tmp = "Process " + path.charAt(path.length() - 1);
           NameID.put(pcb.procID, tmp);

           int st = 0;
           System.out.println(Arrays.toString(memory.valid));
           while (memory.valid[st] != null) st++;
           System.out.println("path="+path);
           memory.memo[st + 0] = pcb.procID + "";
           memory.memo[st + 1] = pcb.procState + "";
           memory.memo[st + 2] = pcb.ProgramCounter + "";
           memory.memo[st + 3] = pcb.MemBoundaries + "";
           memory.valid[st + 0] = NameID.get(pcb.procID);
           memory.valid[st + 1] = NameID.get(pcb.procID);
           memory.valid[st + 2] = NameID.get(pcb.procID);
           memory.valid[st + 3] = NameID.get(pcb.procID);


//       path = "F:\\OS_22_Project" + "\\" + path ;


       try {
           File file = new File(path);
           Scanner input = new Scanner(file);


           while (input.hasNextLine()) {
               String s = input.nextLine();
               System.out.println("s="+s);
               memory.memo[start] = s;
               memory.valid[start] = NameID.get(pcb.procID);
               start++;
           }
           for (int i = 0; i < 3; i++) {
               memory.valid[start+i] = NameID.get(pcb.procID);
           }
           System.out.println("memory after adding proc: "+Arrays.toString(memory.memo));
           input.close();
       } catch (Exception ex) {
           ex.printStackTrace();
       }

           ready.add(new Process(curTimeSlice,pcb));
   }
       System.out.println(Arrays.toString(memory.memo));

   }
   public ArrayList<String> load() throws Exception{     //from disk to RAM
       ArrayList<String> tmp=new ArrayList<>();
       String path = "F:\\OS_22_Project" + "\\" + "disk" + ".txt";
       Scanner input=new Scanner(new File(path));
       while (input.hasNextLine())
           tmp.add(input.nextLine());
       PrintWriter writer = new PrintWriter("F:\\OS_22_Project"+"\\"+"disk"+".txt");
       writer.print("");
       writer.close();


      return tmp;
   }
   public void unload(){              //from RAM to disk
       String path="F:\\OS_22_Project"+"\\"+"disk"+".txt";
       try {
           BufferedWriter bw=new BufferedWriter(new FileWriter(path));
           int p=39;
           while (memory.valid[p]==null)p--;
           String s=memory.valid[p];
           for (int i = 12; i <40 ; i++) {
               if(memory.valid[i].equals(s)){
                   bw.write(memory.memo[i]+ "\n");
                   memory.memo[i]=null;
                   memory.valid[i]=null;
               }

           }

           bw.close();
       }catch (Exception ex){
           return;
       }

   }
   public String getInput(){
           Scanner sc=new Scanner(System.in);
           System.out.println("Please Enter a Value");
           String ss=sc.next();
           return ss;

   }
    public  void print(String s){
       int r1=0;
        switch (s){
            case "a":r1=-2; break;
            case "b":r1=-1; break;
            case "c":r1=0; break;
        }
       System.out.println(memory.memo[running.pcb.MemBoundaries.end+r1]!=null?memory.memo[running.pcb.MemBoundaries.end+r1]:s);
    }
    public  void assign(String var,String val){
        int r1=0;
        switch (var){
            case "a":r1=-2; break;
            case "b":r1=-1; break;
            case "c":r1=0; break;
        }
        memory.memo[running.pcb.MemBoundaries.end+r1]=val;
    }
    public  void writeFile(String path,String s){
        int r1=0;
        switch (path){
            case "a":r1=-2; break;
            case "b":r1=-1; break;
            case "c":r1=0; break;
        }
        int r2=0;
        switch (s){
            case "a":r2=-2; break;
            case "b":r2=-1; break;
            case "c":r2=0; break;
        }
        s= memory.memo[running.pcb.MemBoundaries.end+r2];
        path= memory.memo[running.pcb.MemBoundaries.end+r1];
        path="F:\\OS_22_Project"+"\\"+path+".txt";

        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(path));
            bw.write(s+ "\n");
            bw.close();
        }catch (Exception ex){
            return;
        }

    }
    public  String readFile(String path){
        int r1=0;
        switch (path){
            case "a":r1=-2; break;
            case "b":r1=-1; break;
            case "c":r1=0; break;
        }
        path= memory.memo[running.pcb.MemBoundaries.end+r1];
        path="F:\\OS_22_Project"+"\\"+path+".txt";
        try {
            BufferedReader br=new BufferedReader(new FileReader(path));

            String s= br.readLine();
            br.close();
            return s;
        }catch (Exception ex){
            return "An error";
        }

    }
    public void output(String s){
        System.out.println("*************************");
       switch (s){
           case "dispatch":System.out.println("After Dispatching");break;
           case "preempt": System.out.println("After Preemption");break;
           case "block" : System.out.println("After Blocking");break;
           case "finish" : System.out.println("After Finishing");break;
           case "terminate": System.out.println("At Termination"); break;
           case "begin": System.out.println("At Begining");break;
           default: System.out.println("Undefined Term: "+s);break;
       }
        System.out.println("Ready Queue: "+ready);
        System.out.println("Blocked Queue: "+blocked);
        System.out.println("blockedOn: "+blockedOn.toString());
        System.out.println("resources: "+resources.toString());
        System.out.println("*************************");
    }
    public String getProcName(Process p){
       return NameID.get(p.pcb.procID);
    }
}
