package hangman;

import java.io.File;

public class Main {


    public static void  main(String[] args)
    {

        File dictionary = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        EvilHangman Hangman = new EvilHangman(guesses);
        Hangman.startGame(dictionary,wordLength);
        while (Hangman.continued())
        {
            Hangman.display();
        }
        if(Hangman.win)
        {
            System.out.printf("You win! %s",Hangman.wordBank.first());
        }
        else
        {
            System.out.printf("You lose!\nThe word was: %s\n",Hangman.wordBank.first());
        }

//Hangman.test();

    }


}
