public class Score {
    private int numGuess;
    private String name;
    private int rank;

    //constructor for when reading from the file
    public Score (int rank, String name, int numGuess){
        this.numGuess = numGuess;
        this.name = name;
        this.rank = rank;
    }

    //Constructor for when the rank is unknown
    public Score(int numGuess, String name){
        this.numGuess = numGuess;
        this.name = name;
    }

    //Initial constructor just in case
    public Score(){
        this.numGuess = 10;
        this.name = "   ";
        this.rank = 10;
    }

    public String toString(){
        return rank + "\t" + name + "\t" + numGuess;
    }

    public int getNumGuess() {
        return numGuess;
    }

    public void setNumGuess(int numGuess) {
        this.numGuess = numGuess;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
