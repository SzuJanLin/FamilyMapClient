package hangman;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangman implements IEvilHangmanGame
{
    public int guesses;
    private TreeSet<String> wordBank = new TreeSet<>();
    private TreeSet<String> usedWords = new TreeSet<>();
    private String word = new String();
    EvilHangman(){};
    EvilHangman(int guesses){this.guesses = guesses; };
    public static class GuessAlreadyMadeException extends Exception {
    }

    public void startGame(File dictionary, int wordLength)
    {
        try
        {
            FileReader read = new FileReader(dictionary.getPath());
            Scanner in = new Scanner(read);
            TreeSet<String> OriginalBank = new TreeSet<>();
            while (in.hasNextLine())
            {
                OriginalBank.add(in.nextLine());
            }
            for(String c : OriginalBank)
            {
                if(c.length() == wordLength)
                {
                    wordBank.add(c);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean continued ()
    {
        if (guesses>0)
            return true;
        else
            return false;
    }

    public void driver()
    {
        System.out.printf("You have %d guesses left\n",guesses);
        System.out.print("Used letters:");
        for (String c : usedWords)
        {
            System.out.printf(" %s",c);
        }
        System.out.printf("Word: %s",word);
        do {
            try {
                Scanner in = new Scanner(System.in);
                if (in != null) {
                    in.next();
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
        while (valid());



    }

    private boolean valid()
    {
        return true;
    }

    public void validGuess()
    {
        System.out.print("Enter guess: ");

    }


    /**
     * Make a guess in the current game.
     *
     * @param guess The character being guessed
     *
     * @return The set of strings that satisfy all the guesses made so far
     * in the game, including the guess made in this call. The game could claim
     * that any of these words had been the secret word for the whole game.
     *
     * @throws IEvilHangmanGame.GuessAlreadyMadeException If the character <code>guess</code>
     * has already been guessed in this game.
     */
    public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException
    {
        return null;
    }

}
