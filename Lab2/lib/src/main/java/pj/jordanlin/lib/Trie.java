package pj.jordanlin.lib;

public class Trie implements ITrie {

    public int wordCount = 0;
    public int nodeCount = 1;

    public  WordNode root = new WordNode();
    public void add(String word)
    {
        char[] array = word.toCharArray();
        addHelper(root,array,0);

    }
    public  void addHelper(WordNode curNode, char[] curArray,int it)
    {
        char insert = curArray[it];
        if(curNode.node[insert-'a'] == null)
        {
            curNode.node[insert-'a'] = new WordNode();
            nodeCount++;
        }
        if(it == curArray.length-1)
        {
            curNode.node[insert-'a'].count++;
            if(curNode.node[insert-'a'].count < 2)
                wordCount++;
        }
        else
        {
            it++;
            addHelper(curNode.node[insert-'a'],curArray,it);
        }

    }

    public ITrie.INode find(String word)
    {
        if(word == null)
            return null;
        char[] array = word.toCharArray();
        WordNode curNode = root;
        for(int i = 0; i < array.length; i++)
        {
            char ch = array[i];
            if(curNode.node[ch-'a'] == null)
                return null;
            else
            {
                curNode = curNode.node[ch-'a'];
                if(curNode.count>0 && (ch == array[array.length-1]) && (i == array.length-1))
                {
                    return curNode;
                }
            }
        }
        return  null;
    }

    public int getWordCount()
    {
        return wordCount;
    }

    public int getNodeCount()
    {
        return nodeCount;
    }
    private  void toString_Helper(WordNode curNode, StringBuilder curWord , StringBuilder output)
    {
        if(curNode.count>0)
        {
            output.append(curWord.toString()+"\n");
        }
        if(curNode.node!= null)
        {
            for(int i = 0; i < curNode.node.length; ++i)
            {
                WordNode curChild = curNode.node[i];
                if(curChild != null)
                {
                    curWord.append((char)('a' + i));
                    toString_Helper(curChild,curWord,output);
                    curWord.setLength(curWord.length()-1);
                }
            }
        }

    }
    @Override
    public String toString()
    {
        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toString_Helper(root,curWord,output);
        return output.toString();
    }


    @Override
    public int hashCode() {
        return  (nodeCount+wordCount)*13;
    }

    @Override
    public boolean equals(Object o)
    {
        if( o == null)
            return  false;
        if (o == this)
            return  true;
        if(getClass() != o.getClass())
            return false;
        Trie p = (Trie) o;
        if(p.getNodeCount() == getNodeCount()&& (p.getWordCount() == getWordCount()))
        {
            return NodeChecks(root,p.root);
        }
        else
            return false;

    }

    private boolean NodeChecks(WordNode myNode, WordNode yourNode)
    {
        boolean answer = true;

        for(int i =0; i< myNode.node.length; i ++)
        {
            if(myNode.node[i] != null)
            {
                if(yourNode.node[i] == null)
                    return false;
                else
                {
                    if(!myNode.NodeEquals(yourNode))//the frequency the the word
                    {
                        return false;
                    }
                    answer = NodeChecks(myNode.node[i],yourNode.node[i]);
                }

            }
            else
                {
                    if (yourNode.node[i] != null)
                    return false;
            }
        }
        return answer;

    }
}

 class WordNode implements ITrie.INode {


    public WordNode[] node = new WordNode[26];
    public int count;


    public int getValue() {
        return count;
    }

     public boolean NodeEquals(WordNode o)
     {
        if(getValue() == o.getValue())
            return true;
        else
            return false;
     }
}