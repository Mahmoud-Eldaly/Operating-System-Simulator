public class Triple {
    String resName;
    boolean available;
    int lockProc;

    public Triple(String name,boolean ava,int id){
        resName=name;
        available=ava;
        lockProc=id;
    }
    public String toString(){
        return "("+resName+","+available+","+lockProc+")";
    }


}
