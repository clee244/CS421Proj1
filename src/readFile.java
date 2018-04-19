import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;


public class readFile {

    public static List<String> getTokens(String text)
    {
        Properties p = new Properties();
        p.setProperty("annotators", "tokenize");
        StanfordCoreNLP parser = new StanfordCoreNLP(p);

        Annotation doc = new Annotation(text);

        parser.annotate(doc);
        List<CoreLabel> tokens = doc.get(TokensAnnotation.class);

        List<String> tokenList = new ArrayList<String>();

        for(CoreLabel token: tokens)
        {
            String word = token.getString(TextAnnotation.class);
            tokenList.add(word);
        }

        return tokenList;
    }

    public static List<String> getPOSTags(String text)
    {
        Properties p = new Properties();
        p.setProperty("annotators", "tokenize");
        StanfordCoreNLP parser = new StanfordCoreNLP(p);

        Annotation doc = new Annotation(text);

        parser.annotate(doc);
        List<CoreLabel> tokens = doc.get(TokensAnnotation.class);

        List<String> posList = new ArrayList<String>();

        for(CoreLabel token: tokens)
        {
            String pos = token.get(PartOfSpeechAnnotation.class);
            posList.add(pos);
        }

        return posList;
    }

    public static List<String> getSentences(String text)
    {
        Properties p = new Properties();
        p.setProperty("annotators", "tokenize, ssplit");
        StanfordCoreNLP parser = new StanfordCoreNLP(p);

        Annotation doc = new Annotation(text);

        parser.annotate(doc);

        List<CoreMap> sentences = doc.get(SentencesAnnotation.class);

        List<String> sentenceList = new ArrayList<String>();

        for(CoreMap sentence: sentences)
        {
            String sentenceText = sentence.get(TextAnnotation.class);
            sentenceList.add(sentenceText);
        }

        return sentenceList;
    }

    /* Takes in the number of an essay's sentences and returns an integer score based on that number */
    public static int getaScore(int numSentences)
    {
        int temp = 0;

        if(numSentences >= 0 && numSentences < 10)
            temp = 1;
        else if(numSentences >= 10 && numSentences < 13)
            temp = 2;
        else if(numSentences >= 13 && numSentences < 16)
            temp = 3;
        else if(numSentences >= 16 && numSentences < 19)
            temp = 4;
        else if(numSentences >= 19)
            temp = 5;

        return temp;
    }

    /* Takes in the number of spelling mistakes and assigns a score based on that number */

    public static int getbScore(int numOfSpellingMistakes)
    {
        int temp = 0;

        //Divide by 2 to account for words not being contained in the dictionary used(i.e. names not included and such)
        if(numOfSpellingMistakes > 40)
        {
            temp = 0;
        }
        else if(numOfSpellingMistakes >= 30 && numOfSpellingMistakes <= 40)
        {
            temp = 1;
        }
        else if(numOfSpellingMistakes >= 20 && numOfSpellingMistakes <= 30)
        {
            temp = 2;
        }
        else if(numOfSpellingMistakes >= 10 && numOfSpellingMistakes <= 20)
        {
            temp = 3;
        }
        else if(numOfSpellingMistakes >= 0 && numOfSpellingMistakes <= 10)
        {
            temp = 4;
        }

        return temp;
    }

    public static int getNumOfSpellingErrors(List<String> tokens)
    {
        int numOfErrors = 0;
        int check = 0;
        String text = "";

        String dictionary_path = "../CS421Proj1/input/training/dictionary.txt";

        //Reads bytes of file and converts to string
        try {
            text = new String(Files.readAllBytes(Paths.get(dictionary_path)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        List<String> dictionaryTokens = getTokens(text);

        for( String token : tokens)
        {
            //System.out.println(tokens.nextToken());
            for(String dictionaryToken: dictionaryTokens)
            {
                if(token.equals(dictionaryToken) || token.equals(".") || token.equals(",") ||
                        token.equals(";") || token.equals(":") || token.equals("!") ||
                        token.equals("?") || token.equals("(") || token.equals(")") ||
                        token.equals("'s") || token.equals("'"))
                {
                    check = 0;
                    break;
                }
                check = 1;
            }
            if(check == 1)
            {
                numOfErrors++;
            }
        }

        return numOfErrors;
    }

    public static int getNumOfAgreementErrors(String text)
    {
        int temp = 0;

        List<String> tokens = getTokens(text);
        List<String> posTags = getPOSTags(text);

        for(int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);
            String posTag = posTags.get(index);

            System.out.print("[" + token + "/" + posTag + "] ");
        }
        return temp;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String files;

        String training_file_path = "../CS421Proj1/input/training/essays/";
        String results_file_path = "../CS421Proj1/output/results.txt";
        System.out.println("Opening a .txt at relative path " + training_file_path);

        File textFile = new File(training_file_path);
        File resultsFile = new File(results_file_path);

        PrintWriter p = new PrintWriter(resultsFile);

        File[] listOfFiles = textFile.listFiles();

        int numOfSentences = 0;
        int numOfSpellingMistakes = 0;
        int aScore    = 0;
        int bScore    = 0;
        int ciScore   = 0;
        int ciiScore  = 0;
        int ciiiScore = 0;
        int diScore   = 0;
        int diiScore  = 0;
        int finalScore = 0;
        String ranking = "unknown";

        for (int i = 0; i < listOfFiles.length; i++) {
            files = listOfFiles[i].getName();
            String curFile = new String(training_file_path + File.separator + files);

            String text = "";

            //Reads bytes of file and converts to string
            try {
                text = new String(Files.readAllBytes(Paths.get(curFile)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //Using the StanfordCoreNLP parser we obtain Lists containing tokens, POS tags, and sentences.
            List<String> tokens = getTokens(text);
            List<String> posTags = getPOSTags(text);
            List<String> sentences = getSentences(text);


            //Deals with scoring for number of sentences and length
            numOfSentences = sentences.size();
            aScore = getaScore(numOfSentences);

            //Deals with counting spelling mistakes
            numOfSpellingMistakes = getNumOfSpellingErrors(tokens);
            bScore = getbScore(numOfSpellingMistakes);

            //Deals with Agreement mistakes

            System.out.println(listOfFiles[i].getName());
            System.out.println("This essay scored: " + aScore + " out of 5 for having " + numOfSentences + " sentences.");
            System.out.println("This essay scored: " + bScore + " out of 5 for having " + numOfSpellingMistakes + " spelling mistakes.");

            finalScore = (2 * aScore) - bScore + ciScore + ciiScore + (2 * ciiiScore) + (2 * diScore);

            if(finalScore > 10)
            {
                ranking = "high";
            }
            else
            {
                ranking = "low";
            }

            p.printf(listOfFiles[i].getName() + ";" + aScore + ";" + bScore + ";" + ciScore + ";" + ciiScore + ";"
                    + ciiiScore + ";" + diScore + ";" + diiScore + ";" + finalScore + ";" + ranking + "\n");
        }
        p.close();
    }
}