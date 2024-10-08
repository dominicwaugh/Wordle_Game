import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class SequenceGuesser {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    //to run, there must be a command line input
    //first cd src
    //next javac SequenceGuesser.java
    //and then java SequenceGuesser nameOfFileToUse.txt
    //this will successfully run the program below
    public static void main(String[] args) {
//        int[] arr = evaluateGuess("taste", "haste");
//        for(int i = 0; i < arr.length; i++) System.out.print(arr[i]);
//        System.exit(-1);
//        Testing for occurences

        Scanner scnr = new Scanner(System.in);

        String fileName = args[0];
        ArrayList<String> sequences = readSequencesFromFile(fileName);
        System.out.println("Reading sequence from file: " + fileName);

        Random rng = new Random(); //creates the rng
        String targetSequence = "";
        ArrayList<String> filteredSequences = new ArrayList<>();



        //goes through each sequence in the given length of 5
                for (String sequence : sequences) {
                    if (sequence.length() == 5)
                        filteredSequences.add(sequence);
                }

            //If the filteredSequence ArrayList isn't empty, it displays the length of the target sequence that was selected
            //Otherwise it will tell the user that the program did not find any sequences with that length, so it will select one at random
            if (!filteredSequences.isEmpty()) {
                System.out.println("You will be guessing words with length 5");
                targetSequence = filteredSequences.get(rng.nextInt(filteredSequences.size() - 1));
                sequences = filteredSequences;
            } else {
                System.out.println("There are no sequences with length 5 in this file.");
                System.exit(-1);
            }


        //creates an array to store the checked array
        int[] checkedSequence = new int[targetSequence.length()];
        int numGuess = 0;

        //runs till the win condition is set or there are more than 6 attempts
        while(!checkWin(checkedSequence) && numGuess < 6){
            numGuess++;

            //runs for each guess and repeats if the guess is invalid
            System.out.print("Enter your guess (" + numGuess + "): ");
            String guess = scnr.next();
            if(guess.length() != 5){
                System.out.println("This sequence has more/less than 5 characters");
                numGuess--;
                continue;
            }
            if(isSorted(sequences)) {
                if (isValidSequence_binary(sequences, guess, 0, sequences.size()) < 0) {
                    numGuess--;
                    continue;
                }
            } else {
                if (isValidSequence_rec(sequences, guess, 0, sequences.size() - 1) < 0) {
                    numGuess--;
                    continue;
                }
            }

            //stores the checked sequence and displays the feedback
            checkedSequence = evaluateGuess(guess, targetSequence);
            displayFeedback(guess, checkedSequence);
        }

        if(numGuess >= 6){ //Once win conditions are met, the final display is staged.
            System.out.println("Tough luck! You ran out of guesses. The correct sequence was: " + targetSequence);
        } else {
            System.out.println("Congratulations! You guessed the correct sequence in " + numGuess + " attempts.");
        }

        System.out.print("Pleaser enter a 3 character name: ");
        String name = scnr.next();
        while(name.length() != 3){
            System.out.println("Please enter in the correct name format: ");
            name = scnr.next();
        }
        System.out.println("    Leaderboard    ");
        System.out.println("Rank\tName\tScore");
        try{
            updateLeaderboard(name, numGuess, "leaderboard.txt");
            Scanner fileReader = new Scanner(new FileInputStream("leaderboard.txt"));
            while(fileReader.hasNextLine()){
                System.out.println(fileReader.nextLine());
            }
        } catch(FileNotFoundException e){}



        //gets and stores user feedback
        try{
            System.out.print("Please enter any feedback you have for the program: ");
            FileOutputStream feedbackFile = new FileOutputStream("Feedback.txt", true);
            PrintWriter fileWriter = new PrintWriter(feedbackFile);
            scnr.nextLine();
            System.out.println();
            fileWriter.println(scnr.nextLine());
            fileWriter.close();
            feedbackFile.close();
        } catch(FileNotFoundException e){} catch (IOException e) {}

        System.out.println("Thank you for playing!");
    }


    //This method will take the filename as input, read the file, and
    //return an ArrayList<String> containing each number sequence
    //from the file.
    public static ArrayList<String> readSequencesFromFile(String fileName){
        ArrayList<String> sequence = new ArrayList<>();
        FileInputStream myFile = null;
        Scanner fileReader = null;
        try {
            myFile = new FileInputStream(fileName);
            fileReader = new Scanner(myFile);

            while(fileReader.hasNextLine()){
                sequence.add(fileReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Please enter a correct file name");
            System.exit(0);
        }

        return sequence;
    }


//    This method takes the list of number sequences as input and;
//1) selects a random sequence from the list, 2) reads the guessed
//sequence from the user, and 3) checks if the guessed sequence
//exists in the provided list of sequences and returns true if it exists,
//false otherwise.
    //this is the new recursive version
    public static int isValidSequence_rec(ArrayList<String> sequences, String guessed, int low, int high){
            //returns true when the sequence is found, and it is the same length as the target sequence
            if(low <= high){
                if(sequences.get(low).equals(guessed))
                    return low;
                else if (sequences.get(high).equals(guessed))
                    return high;
                return isValidSequence_rec(sequences, guessed, low + 1, high - 1);
            } else {
                System.out.println("Please enter a valid sequence");
                return -1;
            }
    }




    public static int isValidSequence_binary(ArrayList<String> sequences, String guessed, int low, int high){
        if(low > high) return -1;
        int middle = (high + low) / 2;
        if (sequences.get(middle).equals(guessed))
            return middle;
        else if(guessed.compareToIgnoreCase(sequences.get(middle)) < 0){
            return isValidSequence_binary(sequences, guessed, low, middle - 1);
        } else {
            return isValidSequence_binary(sequences, guessed, middle + 1, high);
        }
    }




    //checks if the list is sorted
    public static boolean isSorted(ArrayList<String> sequences){
        for(int i = 0; i < sequences.size() - 1; i++){
         if(sequences.get(i).compareToIgnoreCase(sequences.get(i + 1)) > 0)
            return false;
        }
        return true;
    }



    //This method takes the guessed sequence and the target sequence,
    //compares them, and returns an integer array where:
    //– 2 represents a correct number in the correct position (green).
    //– 1 represents a correct number in the wrong position (yellow).
    //– 0 represents a number not in the sequence (gray).
    public static int[] evaluateGuess(String guess, String target){
        int[] evaluation = new int[guess.length()]; //creates a double array of ints to store checks
        int[] targetArray = new int[target.length()]; // creates an array to determine if the character has been taken already


        //goes through each character
        for(int i = 0; i < guess.length(); i++){
            //if the target character is the same as the guess character, it is noted in both arrays
            if(i < target.length() && guess.charAt(i) == target.charAt(i)) {
                if(targetArray[i] > 0){
                    evaluation[targetArray[i] - 1] = 0;
                }
                evaluation[i] = 2;
                targetArray[i] = i + 1;
            } else {
                //Goes through each other character and checks if it has already been checked or if it should be  assigned to the new character
                for(int j = 0; j < target.length(); j++){
                    if(guess.charAt(i) == target.charAt(j) && targetArray[j] == 0){
                        targetArray[j] = i + 1;
                        evaluation[i] = 1;
                        break;
                    }
                }
            }
        }

        return evaluation;
    }


    //Goes through each character and displays it in the correct color
    public static void displayFeedback(String guess, int[] feedback){
        for(int i = 0; i < guess.length(); i++){
            if(feedback[i] == 2)
                System.out.print(ANSI_GREEN_BACKGROUND + ANSI_BLACK + guess.charAt(i));
            else if (feedback[i] == 1)
                System.out.print(ANSI_YELLOW_BACKGROUND + ANSI_BLACK + guess.charAt(i));
            else
                System.out.print(ANSI_WHITE_BACKGROUND + ANSI_BLACK + guess.charAt(i));

        }
        System.out.println(ANSI_RESET);
    }



    //Checks to see if the user has won by checking to see if all the characters are 2
    public static boolean checkWin(int[] feedback){
        for(int i = 0; i < feedback.length; i++){
            if(feedback[i] != 2)
                return false;
        }
        return true;
    }



    //leaderboard that tracks the top five best performances based on the fewest number of guesses, by reading and writing to a file.
    public static void updateLeaderboard(String userName, int numGuess, String fileName){
        ArrayList<Score> oldLeaderboard = new ArrayList<>();
        ArrayList<Score> newLeaderboard = new ArrayList<>();
        Score newScore = new Score(numGuess, userName);

        //reads from the existing leaderboard file
        FileInputStream oldLeaderboardFile = null;
        Scanner fileReader = null;
        try {
            oldLeaderboardFile = new FileInputStream(fileName);
            fileReader = new Scanner(oldLeaderboardFile);
        } catch (FileNotFoundException e){
            System.out.println("Leaderboard file not found. Continuing sequence.");
            return;
        }


        //adds the Score objects for all the objects in the file
        while(fileReader.hasNext()) {
            int rank = Integer.parseInt(fileReader.next());
            String name = fileReader.next();
            int newNumGuess = Integer.parseInt(fileReader.next());
            oldLeaderboard.add(new Score(rank, name, newNumGuess));
        }


        //goes through each score on the leaderboard and compares it to the new score
        for(int i = 0; i < 5; i++){
            if(oldLeaderboard.size() < i+1 || numGuess <= oldLeaderboard.get(i).getNumGuess()){
                newScore.setRank(i + 1); //changes the rank of the new score once it beats the old score
                newLeaderboard.add(newScore);
                for(int j = i; j < oldLeaderboard.size() && j < 4; j++){
                    oldLeaderboard.get(j).setRank(j + 2); //shifts all the ranks down one
                    newLeaderboard.add(oldLeaderboard.get(j)); //adds the rest of the  ranks to the new leaderboard
                }
                break; //breaks once all 5 slots are filled
            } else {
                newLeaderboard.add(oldLeaderboard.get(i));
            }
        }

        //updates the leaderboard file to the new leaderboard from the ArrayList
        PrintWriter fileWriter = null;
        FileOutputStream newLeaderboardFile = null;
        try {
            newLeaderboardFile = new FileOutputStream(fileName);
            fileWriter = new PrintWriter(newLeaderboardFile);
            for (Score score : newLeaderboard) {
                fileWriter.println(score);
            }
            fileWriter.close();
            newLeaderboardFile.close();
            fileReader.close();
        } catch (IOException e){}
    }

}
