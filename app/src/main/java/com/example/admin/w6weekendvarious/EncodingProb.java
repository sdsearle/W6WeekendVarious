package com.example.admin.w6weekendvarious;

/**
 * Created by admin on 10/6/2017.
 */

public class EncodingProb {
    private static String[] alphabet = new String[26];
    private static int[] alphabetId = new int[26];
    private static String myString = "Hello there! Apple!";

    public static void main(String[] args) {

        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i]= Character.toString((char) ('a'+i));
            //System.out.println(alphabet[i]);
        }

        myString = myString.toLowerCase();
        inverse(myString);
        frequency(myString);

    }

    private static void frequency(String myString) {
        for (int i = 0; i < myString.length(); i++) {
            int id = (myString.charAt(i) - 'a');
            if(id >= 0 && id <= 25)
            alphabetId[id] += 1;
        }

        for (int i = 0; i < alphabet.length; i++) {
            System.out.println(alphabet[i] + ": " + alphabetId[i]);
        }
    }

    private static void inverse(String myString) {
        String inverse = "";
        for (int i = 0; i < myString.length(); i++) {
            inverse += ((char)('z' - myString.charAt(i)+'a'));
        }

        System.out.println(inverse);
    }
}
