import java.io.*;
import java.util.*;


public class ScabbleGame {
    public static void main(String[] args) {
        List<String> wordList = loadWordList("CollinsScrabbleWords_2019.txt"); //load wordlist txt file

        Collections.sort(wordList); //sort it just in case

        String userWord = "redd"; //currently using just random word 

        boolean found = binarySearch(wordList, userWord); //binary search

        if (found) {
            System.out.println("Found: " + userWord);
        } else {
            System.out.println("Not found: " + userWord);
        }
    }
    //function that creates a list from the file, line by line
    public static List<String> loadWordList(String fileName) {
        List<String> wordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {//300k is a lot so buffered reader is most efficient way to turn it into an array list
            String word;
            while ((word = br.readLine()) != null) {
                wordList.add(word); //word added for each line read, until there is no lines left (null case)
            }
        }catch (IOException e) {
            e.printStackTrace(); 
        }
        return wordList;
    }
    //binary search function (efficient)
    public static boolean binarySearch(List<String> sortedList, String targetWord) {
        int left = 0; //left side index/pointer
        int right = sortedList.size() - 1; //right side index/pointer
        targetWord = targetWord.toUpperCase(); //to make it case insensitive (word list is all uppercase)

        while(left <= right) { //searches until the left/right index meet or pass each other
            int mid = left + (right - left) / 2; //middle index
            int comparison = targetWord.compareTo(sortedList.get(mid)); //compares the target word to the current middle word 

            if (comparison == 0) { //0 means they are equal, so the word is found
                return true;
            }else if (comparison > 0){ //greater than 0 (0 is the middle word) means its on the right side of, we change mid to the middle of the right side 
                left = mid + 1;//left side cap is now at mid+1
            } else { //less than 0 means its on the left side, so mid becomes middle of left side
                right = mid - 1; //right side cap is now at mid-1
            }
        } 
        return false;
    }
}