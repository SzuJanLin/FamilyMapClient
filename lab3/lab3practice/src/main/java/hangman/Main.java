package hangman;

import java.io.File;

public class Main {


    public static void  main(String[] args)
    {

        File dictionary = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        IEvilHangmanGame Hangman = null;
        Hangman.startGame(dictionary,wordLength);

//Hangman.test();

    }


}
