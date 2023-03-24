import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Parker Brown
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);

    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        String[] words1 = new String[words.size()];
        int i = 0;
        // Iterates through words arrayList to create copy
        for (String word : words) {
            words1[i] = word;
            i++;
        }
        // Calls merge algorithm to create sorted array
        String[] merged = mergeSort(words1, 0, words.size() - 1);
        // Empties words arrayList
        words.clear();
        // Fills words ArrayList with sorted array
        for (String word : merged) {
            words.add(word);
        }
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        int i = 0;
        // Iterates through each word
        while (i < words.size()) {
            String word = words.get(i);
            // Checks to see if word is in DICTIONARY using binary search
            if(!binary(word, 0, DICTIONARY_SIZE - 1)) {
                // Removes word from array if not found
                words.remove(i);
                i--;
            }
            i++;
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
    // generates words with given letters
    public void makeWords(String product, String lettersLeft) {
        // Adds product to list of words
        words.add(product);
        // Base case: if there are no more letters
        if (lettersLeft.equals("")) {
            return;
        }
        // For every letter, recall make words
        for (int i = 0; i < lettersLeft.length(); i++) {
            makeWords(product + lettersLeft.charAt(i), lettersLeft.substring(0, i) + lettersLeft.substring(i + 1));
        }
    }
    // sorts words using mergesort
    public String[] mergeSort(String[] arr, int low, int high) {
        // Base case: if high and low are the same
        if (high - low == 0) {
            String[] newArr = new String[1];
            newArr[0] = arr[low];
            return newArr;
        }
        int med = (high + low) / 2;
        // Recursive step: recall merge sort on two smaller arrays
        String[] arr1 = mergeSort(arr, low, med);
        String[] arr2 = mergeSort(arr, med + 1, high);
        return merge(arr1, arr2);
    }
    // continues the implementation of merge sort
    public String[] merge(String[] arr1, String[] arr2) {
        // merged contains final sorted array
        String[] merged = new String[arr1.length + arr2.length];
        int i = 0, j = 0;
        // while there are still contents in both arrays
        while (i < arr1.length && j < arr2.length) {
            // uses compareTo to determine alphabetical order
            if (arr1[i].compareTo(arr2[j]) < 0) {
                merged[i + j] = arr1[i];
                i++;
            }
            else {
                merged[i + j] = arr2[j];
                j++;
            }
        }
        // If there are still words left in one of the arrays, they are added
        while (i < arr1.length) {
            merged[i + j] = arr1[i];
            i++;
        }
        while (j < arr2.length) {
            merged[i + j] = arr2[j];
            j++;
        }
        return merged;
    }
    // implementation of binary sort to search for word in dictionary
    public boolean binary(String word, int low, int high) {
        // Base case: high and low refer to same
        if (high - low <= 0) {
            // if given word is in dictionary return true
            if (DICTIONARY[low].equals(word)) {
                return true;
            }
            return false;
        }
        // cuts problem in half by subtracting side that word is known to not be on
        int med = (high + low) / 2;
        // Recursive step: looks for word on either side
        return binary(word, low, med - 1) || binary(word, med + 1, high);
    }
}
