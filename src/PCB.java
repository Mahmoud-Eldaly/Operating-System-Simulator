public class PCB {
    int procID;
    State procState;
    int ProgramCounter;
    bound MemBoundaries;


    public PCB(int id,int start, int end){
        this.procID=id;
        this.procState=State.Ready;
        this.ProgramCounter=0;
        this.MemBoundaries=new bound(start,end);
    }



    static class bound{
        int start;
        int end;


        public bound(int start,int end){
            this.start=start;
            this.end=end;
        }
        public String toString(){
            return start+" to "+end;
        }

    }

}

