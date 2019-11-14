package spell;

import java.io.*;
import java.util.*;
import java.lang.*;


public class SpellCorrector implements ISpellCorrector
{
	ITrie dictionary = new Trie();










	/**
	 * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
	 * for generating suggestions.
	 * @param dictionaryFileName File containing the words to be used
	 * @throws IOException If the file cannot be read
	 */
	public void useDictionary(String dictionaryFileName) throws IOException
    {
    	FileReader read = new FileReader(dictionaryFileName);
    	Scanner in = new Scanner(read);
    	while (in.hasNextLine())
		{
			dictionary.add(in.nextLine());
		}
		in.close();


    }






	/**
	 * Suggest a word from the dictionary that most closely matches
	 * <code>inputWord</code>
	 * @param inputWord
	 * @return The suggestion or null if there is no similar word in the dictionary
	 */
	public String suggestSimilarWord(String inputWord)
    {

    	inputWord = inputWord.toLowerCase();
    	int frequency = 0;
    	String out = null;
    	inputWord = inputWord.toLowerCase();
    	ITrie.INode result = dictionary.find(inputWord);
    	if(result!= null)
		{
			return inputWord;
		}
    	TreeSet<String> wordChoice = new TreeSet<>();

    	wordChoice.addAll(deletion(inputWord));
    	wordChoice.addAll(alteration(inputWord));
    	wordChoice.addAll(transportation(inputWord));
    	wordChoice.addAll(insertion(inputWord));

    	for(String c : wordChoice)
		{
			result = dictionary.find(c);
			if(result!=null&&  result.getValue()>frequency)
			{
				frequency = result.getValue();
				out = c;
			}

		}

		if(result!= null)
		{
			return out;
		}
		TreeSet<String> wordChoice2 = new TreeSet<>();
    	for(String c : wordChoice)
		{
			wordChoice2.addAll(deletion(c));
			wordChoice.addAll(alteration(inputWord));
			wordChoice.addAll(transportation(inputWord));
			wordChoice.addAll(insertion(inputWord));
		}
		for(String c : wordChoice2)
		{
			result = dictionary.find(c);
			if(result!=null&&  result.getValue()>frequency)
			{
				frequency = result.getValue();
				out = c;
			}
		}


    	return out;
    }

    private  TreeSet<String> deletion(String inputWord)
	{
		TreeSet<String> wordChoice = new TreeSet<>();
		if (inputWord.length() == 1)
		{
			return wordChoice;
		}
		for(int i=0; i < inputWord.length(); i++)
		{
			StringBuilder target = new StringBuilder(inputWord);
			target.delete(i,i+1);
			wordChoice.add(target.toString());
		}

		return wordChoice;
	}

	private TreeSet<String> transportation(String inputWord)
	{
		TreeSet<String> wordChoice = new TreeSet<>();
		if (inputWord.length() == 1)
		{
			return wordChoice;
		}
		for(int i=0; i < inputWord.length()-1; i++)
		{
			char[] target = inputWord.toCharArray();
			char temp = target[i];
			target[i] = target[i+1];
			target[i+1] = temp;
			String result = new String(target);
			wordChoice.add(result);
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