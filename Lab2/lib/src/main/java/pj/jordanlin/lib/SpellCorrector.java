package pj.jordanlin.lib;

import java.io.FileReader;
import java.io.IOException;
import java.lang.*;
import java.util.Scanner;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {

    ITrie dictionary = new Trie();
    /**
     * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     *
     * @param dictionaryFileName File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(String dictionaryFileName) throws IOException
    {
        FileReader reader = new FileReader(dictionaryFileName);
        Scanner in = new Scanner((reader));
        while(in.hasNextLine())
        {
            String input = in.nextLine();
            dictionary.add(input);
        }
        in.close();

    }

    public void test()
    {
        System.out.println(dictionary.toString());
    }
    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     *
     * @param inputWord
     * @return The suggestion or null if there is no similar word in the dictionary
     */
    public String suggestSimilarWord(String inputWord) {

        inputWord = inputWord.toLowerCase();


        ITrie.INode curNode = dictionary.find(inputWord);
        if(curNode != null)
        {
            return inputWord;
        }

        String out = null;
        int frequency = 0;
        ITrie.INode aNode = null;


        TreeSet<String> wordChoice = new TreeSet<>();
        wordChoice.addAll(deletion(inputWord));
        wordChoice.addAll(transposition(inputWord));
        wordChoice.addAll(alteration(inputWord));
        wordChoice.addAll(insertion(inputWord));

        for (String target : wordChoice)
        {
             aNode = dictionary.find(target);
             if(aNode != null &&(aNode.getValue()>frequency))
             {
                 out = target;
                 frequency = aNode.getValue();
             }
        }
        aNode = dictionary.find(out);
        if(aNode == null)
        {
            TreeSet<String> wordChoice2 = new TreeSet<>();
            for (String target2 : wordChoice)
            {
                wordChoice2.addAll(deletion(target2));
                wordChoice2.addAll(transposition(target2));
                wordChoice2.addAll(alteration(target2));
                wordChoice2.addAll(insertion(target2));
            }
            for (String target : wordChoice2)
            {
                aNode = dictionary.find(target);
                if(aNode != null &&(aNode.getValue()>frequency))
                {
                    out = target;
                    frequency = aNode.getValue();
                }
            }
        }


        return out;
    }

    private TreeSet<String> deletion(String inputWord)
    {
        TreeSet<String> wordChoice = new TreeSet<>();
        if(inputWord.length() == 1)
            return wordChoice;
        for(int i =0; i< inputWord.length(); i++)
        {
            StringBuilder word = new StringBuilder(inputWord);
            word.delete(i,i+1);
            wordChoice.add(word.toString());
        }
        return  wordChoice;
    }
    private TreeSet<String> transposition(String inputWord)
    {

        TreeSet<String> wordChoice = new TreeSet<>();
        for(int i =0; i< inputWord.length()-1; i++)
        {
            char[] word = inputWord.toCharArray();
            char a = word[i];
            word[i] = word[i+1];
            word[i+1] = a;
            String wordString = new String(word);
            wordChoice.add(wordString);
        }

        return wordChoice;
    }
    private TreeSet<String> alteration(String inputWord)
    {
        TreeSet<String> wordChoice = new TreeSet<>();
        for(int i =0; i< inputWord.length(); i++)
        {
            char[] word = inputWord.toCharArray();
            char a = word[i];
            for(int j = 0; j < 26; j++)
            {
                if(a != 'a'+j)
                {
                    word[i] = (char)('a'+j);
                    String wordString = new String(word);
                    wordChoice.add(wordString);
                }
            }
        }

        return wordChoice;
    }
    private TreeSet<String> insertion (String inputWord)
    {
        TreeSet<String> wordChoice = new TreeSet<>();

        for(int i =0; i< inputWord.length()+1; i++)
        {
            for(int j = 0; j < 26; j++)
            {
                StringBuilder word = new StringBuilder(inputWord);

                word.insert(i,(char)('a'+j));
                wordChoice.add(word.toString());

            }
        }
        return wordChoice;
    }

}