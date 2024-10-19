import java.io.*;
import java.util.*;

//Adam Barakat
//Jose Arellano
//Ethan Smith
//Luis Cruz Pereda

public class ScrabbleGame {
    
    public static void main(String[] args) {
        
        List<Word> wordList = loadWordList("CollinsScrabbleWords_2019.txt"); //load wordlist txt file
        // Collections.sort(wordList); // Sort the list of Word objects

        int numOfLetters = 4; 
        char[] letters = letterGen(numOfLetters);
        String player1Word, player2Word; 
        boolean valid1, valid2;

        Scanner scnr = new Scanner(System.in); // Create scanner instance

        System.out.println("Welcome to ScrabbleGame! Two players will compete for the biggest word.");
        
        do {
            System.out.println("There will be " + numOfLetters + " letters for you to make a word with.");
            System.out.println("Here are your letters: " + Arrays.toString(letters).toUpperCase());
            System.out.println("Player 1, enter your word using only the " + numOfLetters + " letters given, or type 'exchange' to swap a letter, or 'end' to exit:");

            player1Word = scnr.nextLine().toUpperCase(); // Player 1's word

            if (player1Word.equals("END")) { // End game if inputting END
                System.out.println("Thank you for playing! Goodbye!");
                break; // Exit the loop
            }
          //Luis Cruz Pereda
            if (player1Word.equals("EXCHANGE")) { // typing exchange exchanges a letter for another letter
                letters = exchangeLetter(letters, scnr);
                continue; // Continue to the next loop
            }

            valid1 = validityCheck(player1Word, letters);
            if (!valid1) {
                System.out.println("Invalid word. Player 1, please try again.");
                continue;
            }

            System.out.println("Player 2, enter your word using the same letters, or type 'end' to exit:");
            player2Word = scnr.nextLine().toUpperCase(); // Player 2's word

            if (player2Word.equals("END")) {
                System.out.println("Thank you for playing! Goodbye!");
                break;
            }

            valid2 = validityCheck(player2Word, letters);
            if (!valid2) {
                System.out.println("Invalid word. Player 2, please try again.");
                continue;
            }

            //Jose Arellano
            // Binary search to validate both words
            boolean found1 = binarySearch(wordList, player1Word);
            boolean found2 = binarySearch(wordList, player2Word);

            if (!found1) {
                System.out.println("Player 1's word '" + player1Word + "' was not found in the word list.");
            } else {
                System.out.println("Player 1's word '" + player1Word + "' is valid.");
            }

            if (!found2) {
                System.out.println("Player 2's word '" + player2Word + "' was not found in the word list.");
            } else {
                System.out.println("Player 2's word '" + player2Word + "' is valid.");
            }

            //Jose Arellano
            // Compare the lengths or scores
            if (found1 && found2) {
                int player1Score = new Word(player1Word).getScore();
                int player2Score = new Word(player2Word).getScore();

                if (player1Word.length() > player2Word.length()) {
                    System.out.println("Player 1 wins with the word: " + player1Word + " (Score: " + player1Score + ")");
                } else if (player2Word.length() > player1Word.length()) {
                    System.out.println("Player 2 wins with the word: " + player2Word + " (Score: " + player2Score + ")");
                } else {
                    System.out.println("It's a tie! Both words have the same length.");
                }
            } else if (found1) {
                System.out.println("Player 1 wins as Player 2's word was not valid.");
            } else if (found2) {
                System.out.println("Player 2 wins as Player 1's word was not valid.");
            } else {
                System.out.println("Neither player has a valid word. No winner this round.");
            }

        } while (true); // Loop continues until 'end' is typed

        scnr.close(); // Close the scanner when done
    }

    // Function that creates a list from the file, line by line
    public static List<Word> loadWordList(String fileName) {
        List<Word> wordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = br.readLine()) != null) {
                wordList.add(new Word(word)); // Create a Word object and add it to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordList;
    }

    // Binary search function (efficient)
    public static boolean binarySearch(List<Word> sortedList, String targetWord) {
        int left = 0; //left side index/pointer
        int right = sortedList.size() - 1; //right side index/pointer

        while (left <= right) { //searches until the left/right index meet or pass each other
            int mid = left + (right - left) / 2; //middle index
            int comparison = targetWord.compareTo(sortedList.get(mid).getWord()); //compares the target word to the current middle word 

            if (comparison == 0) { //0 means they are equal, so the word is found
                return true; 
            } else if (comparison > 0) { //greater than 0 means its on the right side of, we change mid to the middle of the right side 
                left = mid + 1; //left side cap is now at mid+1
            } else { //less than 0 means its on the left side, so mid becomes middle of left side
                right = mid - 1; //right side cap is now at mid-1
            }
        }
        return false; // Not found
    }

    // Generates 4 random letters for the user to use
    private static char[] letterGen(int numOfLetters) {
        Random random = new Random();
        char[] letters = new char[numOfLetters];

        for (int i = 0; i < numOfLetters; i++) {
            letters[i] = (char) (random.nextInt(26) + 'A'); // Generate uppercase letters
        }

        return letters;
    }

    // Checks if a word is the right length and if it uses the right letters
    private static boolean validityCheck(String userWord, char[] letters) {
        char[] userWordArr = userWord.toCharArray();

        if (userWordArr.length > letters.length) { //returns false if over the limit
            return false;
        }

        boolean[] usedLetters = new boolean[letters.length]; // Track used letters

        for (char c : userWordArr) {
            boolean found = false;
            for (int i = 0; i < letters.length; i++) {
                if (c == letters[i] && !usedLetters[i]) { // Check if letter is available
                    usedLetters[i] = true; // Mark this letter as used
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false; // Letter not found or already used
            }
        }

        return true;
    }

    //Luis Cruz Pereda
    private static char[] exchangeLetter(char[] letters, Scanner scnr) {
        System.out.println("Enter the index (0 to " + (letters.length - 1) + ") of the letter you want to exchange:");
        int index = scnr.nextInt();
        scnr.nextLine(); // Consume the newline character

        if (index < 0 || index >= letters.length) {
            System.out.println("Invalid index. No letter exchanged.");
            return letters;
        }

        letters[index] = letterGen(1)[0]; // Generate a new letter
        System.out.println("Exchanged letter at index " + index + " with '" + letters[index] + "'.");
        return letters;
    }
}
