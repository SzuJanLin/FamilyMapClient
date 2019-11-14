package hangman;

        import java.io.File;
        import java.io.FileReader;
        import java.io.IOException;
        import java.util.HashMap;
        import java.util.HashSet;
        import java.util.LinkedList;
        import java.util.Map;
        import java.util.Scanner;
        import java.util.Set;
        import java.util.TreeSet;

public class EvilHangman implements IEvilHangmanGame
{
    TreeSet<String> wordBank = new TreeSet<>();
    TreeSet<Character> usedWords = new TreeSet<>();
    String word ,bestReturn;
    int guesses=0,worldLength = 0;
    boolean validInput;

    public boolean  win =false;
    public EvilHangman(int guesses)
    {
        this.guesses = guesses;
    }
    public EvilHangman(){}

    @SuppressWarnings("serial")
    public static class GuessAlreadyMadeException extends Exception
    {

    }

    public void startGame(File dictionary, int wordLength)
    {

        this.worldLength = wordLength;
        TreeSet<String> OriginWordBank = new TreeSet<>();
        try {
            FileReader read = new FileReader(dictionary.getPath());
            Scanner in = new Scanner(read);
            while(in.hasNextLine())
            {
                OriginWordBank.add(in.nextLine());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (String word : OriginWordBank)
        {
            if(word.length() == wordLength)
            {
                wordBank.add(word);
            }
        }
        StringBuilder originWord = new StringBuilder();
        for (int i =0 ; i < wordLength; i++)
        {
            originWord.append('-');
        }
        word = originWord.toString();
    }

    public boolean continued ()
    {
        if(guesses > 0 && !win)
        {
            return true;
        }
        else return false;
    }

    public void display()
    {
        System.out.printf("You have %d guesses left\n",guesses);
        guesses--;
        System.out.print("Used letters: ");
        for (char ch: usedWords)
        {
            System.out.printf("%c ",ch);
        }
        System.out.printf("\nWords: %s\n",word);
        String  guess = null;
        do
        {
            System.out.print("Enter guess: ");
            Scanner in = new Scanner(System.in);
            if(in!= null)
                guess = in.next();
            guess = guess.toLowerCase();
            try
            {
                if(guess.length()> 1)
                {

                    System.out.println("Please only choose one letter");
                    validInput = false;
                    throw new GuessAlreadyMadeException();
                }
                else
                {

                    Set<String> tempSet = makeGuess(guess.charAt(0));
                    wordBank = new TreeSet<>(tempSet);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        while (!validInput);

        if(wordBank.size() == 1&& wordBank.first().equals(bestReturn))
        {
            win =true;
        }

        if(win)
        {

        }
        else
        {
            if(word.equals(bestReturn) )
            {
                System.out.printf("Sorry, there are no %c's\n\n",guess.charAt(0));
            }
            else
            {
                word = bestReturn;
                int letterCount = 0;
                for(int i =0; i < bestReturn.length(); i++)
                {
                    if(bestReturn.charAt(i) == guess.charAt(0))
                    {
                        letterCount++;
                    }
                }
                System.out.printf("Yes, there is %d %c\n\n",letterCount, guess.charAt(0));
            }
        }



    }

public void test()
{try {
    Set<String> c;
    makeGuess('m');
    makeGuess('a');
    c = makeGuess('t');
    for (String e: c)
    {
        System.out.println(e);
    }
}
catch (Exception e)
    {

    }

}

    public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException
    {

        if (guess < 'a' || guess>'z')
        {
            validInput = false;
            throw new IEvilHangmanGame.GuessAlreadyMadeException();
        }
        for (char ch : usedWords)
        {
            if(ch == guess)
            {
                validInput = false;
                System.out.println("You already used that letter");
                throw new IEvilHangmanGame.GuessAlreadyMadeException();
            }
        }

        validInput = true;
        usedWords.add(guess);


        Map<String,TreeSet<String>> WordMap = new HashMap<>();
        for (String word : wordBank)
        {
            StringBuilder key = new StringBuilder();
            for(int i=0; i < worldLength; i++ )
            {
                key.append('-');
                for (char ch : usedWords)
                {
                    if(word.charAt(i) == ch)
                        key.setCharAt(i,ch);
                }
            }
            if(!WordMap.containsKey(key.toString()))
            {
                TreeSet<String> tempSet = new TreeSet<String>();
                WordMap.put(key.toString(),tempSet);
            }
            WordMap.get(key.toString()).add(word);
        }
        String bestReturn = pickBest(WordMap,guess);
        this.bestReturn = bestReturn;
        wordBank = WordMap.get(bestReturn);



        return wordBank;
    }

    String pickBest(Map<String,TreeSet<String>> wordMap, char guess)
    {




        TreeSet<String> keySet = new TreeSet<>(wordMap.keySet());

        TreeSet<String> keySet2 = new TreeSet<>();

        String bestKey = null;



        String sizeKey = null;
        int oldSize = 0;
        int newSize = 0;
        LinkedList<Integer> countSize = new LinkedList<Integer>();
        for (String key : keySet)
        {
            newSize = wordMap.get(key).size();
            countSize.add(newSize);

            if (newSize > oldSize)
            {
                sizeKey = key;
                oldSize = newSize;
            }

        }
        int counter = 0;
        for (int i : countSize)
        {
            if(i == oldSize)
            {
                counter++;
            }
        }
        if(counter < 2)
        {
            bestKey = sizeKey;
        }



        if(bestKey == null)
        {
            for (String target : keySet)
            {
                boolean first = true;
                for (int i = 0; i < worldLength; i ++)
                {
                    if(target.charAt(i) == guess)
                        first= false;
                }
                if (first)
                    bestKey = target;
            }
        }

        if(bestKey == null)
        {
            int[] count = new int[worldLength];
            int lastNum = worldLength;
            String key = null;
            int currentNum;
            for (String target : keySet)
            {
                currentNum = 0;
                for (int i = 0; i < worldLength; i++)
                {
                    if (target.charAt(i) == guess)
                        currentNum++;
                }
                count[currentNum]++;

                if (currentNum <= lastNum)
                {
                    key = target;
                    keySet2.add(key);
                    lastNum = currentNum;
                }
            }
            if(count[lastNum] < 2)
            {
                bestKey = key;
            }

        }
        if(bestKey == null)
        {
            outerLoop:
            for(int i = worldLength-1; i > -1; i--)
            {
                for(String target : keySet2)
                {
                    if (target.charAt(i) == guess)
                    {
                        bestKey = target;
                        break outerLoop;
                    }
                }
            }
        }
        return  bestKey;
    }
}
