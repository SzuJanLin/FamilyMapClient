package spell;

import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * Your trie class should implement the ITrie interface
 */
public class Trie implements ITrie {

    int wordCount = 0;
    int nodeCount = 1;
    Node root = new Node();

  

	/**
	 * Adds the specified word to the trie (if necessary) and increments the word's frequency count
	 * 
	 * @param word The word being added to the trie
	 */
    public void add(String word)
    {
        char[] wordChar = word.toCharArray();
        addHelper(root,wordChar,0);

    }
    public void addHelper(Node currentNode, char[] wordChar, int it)
    {
        char letter = wordChar[it];
        if(currentNode.links[letter-'a']== null)
        {
            currentNode.links[letter-'a'] = new Node();
            nodeCount++;
        }

        if(it == wordChar.length-1)
        {
            if(currentNode.links[letter-'a'].getValue() < 1)
            {
                wordCount++;
            }
            
            currentNode.links[letter-'a'].frequency++;
            
        }
        else
        {
            it++;
            addHelper(currentNode.links[letter-'a'],wordChar,it);
        }

    }


	/**s
	 * Searches the trie for the specified word
	 * 
	 * @param word The word being searched for
	 * 
	 * @return A reference to the trie node that represents the word,
	 * 			or null if the word is not in the trie
	 */
    public ITrie.INode find(String word)
    {
     char[] wordChar = word.toCharArray();
     return findHelper(root, wordChar, 0);

    }
    private ITrie.INode findHelper(Node currentNode, char[] wordChar, int it)
	{
		char letter = wordChar[it];
		if(it == wordChar.length-1)
		{
			return currentNode;
		}

		if(currentNode.links[letter-'a']==null)
		{
			return null;
		}
		else
		{
			it++;
			return findHelper(currentNode.links[letter-'a'],wordChar,it);
		}


	}

	
	/**
	 * Returns the number of unique words in the trie
	 * 
	 * @return The number of unique words in the trie
	 */
	public int getWordCount()
	{
		return wordCount;
	}
	
	/**
	 * Returns the number of nodes in the trie
	 * 
	 * @return The number of nodes in the trie
	 */
	public int getNodeCount()
	{
		return nodeCount;
	}
	
	/**
	 * The toString specification is as follows:
	 * For each word, in alphabetical order:
	 * <word>\n
	 */
	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		StringBuilder curWord = new StringBuilder();
		toStringHelper(root,output,curWord);

		return output.toString();
	}
	private void toStringHelper(Node currentNode, StringBuilder output, StringBuilder curWord)
	{
		if(currentNode.getValue()>0)
		{
			output.append(curWord);
			output.append("\n");
		}
		if(currentNode!=null)
		{
			for(int i = 0; i < currentNode.links.length; i++)
			{
				if(currentNode.links[i] != null)
				{
					curWord.append((char)('a'+i));
					toStringHelper(currentNode.links[i],output,curWord);
					curWord.setLength(curWord.length()-1);
				}
			}
		}



	}
	
	@Override
	public int hashCode()
	{
		return 13*(getNodeCount()+getWordCount());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		if(this.getClass() != o.getClass() )
		{
			return false;
		}
		if(this == o)
			return true;
		Trie p = (Trie) o;
		if(p.getWordCount()!=this.getWordCount())
			return false;
		if (p.getNodeCount()!=this.getNodeCount())
			return false;

		return nodeTrace(root,p.root);
	}

	public boolean nodeTrace(Node node1, Node node2)
	{
		boolean answer = true;
		if(node1!= null)
		{
			if (node2 == null)
			{
				return false;
			}
			else
			{
				if(node1.getValue()!=node2.getValue())
				{
					return false;
				}
				for (int i =0; i< node1.links.length; i++)
				{
					answer = nodeTrace(node1.links[i],node2.links[i]);
				}

			}
		}
		else
		{
			if(node2 !=null)
			{
				return false;
			}

		}

		return answer;
	}


	/**
	 * Your trie node class should implement the ITrie.INode interface
	 */
    public class Node implements INode
    {
        Node()
        {
            frequency = 0;
            links = new Node[26];

        }

        int frequency;
        Node[] links;
	
		/**
		 * Returns the frequency count for the word represented by the node
		 * 
		 * @return The frequency count for the word represented by the node
		 */
        public int getValue()
        {
            return frequency;
        }
	}
}
