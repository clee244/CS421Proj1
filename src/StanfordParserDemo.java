import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.*;


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


public class StanfordParserDemo {
	
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
			temp = 0;
		else if(numSentences >= 10 && numSentences < 12)
			temp = 1;
		else if(numSentences >= 12 && numSentences < 14)
			temp = 2;
		else if(numSentences >= 14 && numSentences < 16)
			temp = 3;
		else if(numSentences >= 16 && numSentences < 18)
			temp = 4;
		else if(numSentences >= 18)
			temp = 5;
		
		return temp;
	}
	
	public static int getNumOfSpellingErrors(String text)
	{
		int numOfErrors = 0;
		
		return 0;
	}

    public static void main(String[] args) throws FileNotFoundException {
        //Load test.txt from the resources folder using relative path
        BufferedReader reader = null;
        String files;

        String training_file_path = "../NLPDemo/input/training/essays/";
        String results_file_path = "../NLPDemo/output/results.txt";
        System.out.println("Opening a .txt at relative path " + training_file_path);
        
        File textFile = new File(training_file_path);
        File resultsFile = new File(results_file_path);
        
        PrintWriter p = new PrintWriter(resultsFile);
        
        File[] listOfFiles = textFile.listFiles();
        
        int numOfSentences = 0;
        int aScore    = 0;
        int bScore    = 0;
        int ciScore   = 0;
        int ciiScore  = 0;
        int ciiiScore = 0;
        int diScore   = 0;
        int diiScore  = 0;
        int finalScore = 0;
        String ranking = "unknown";

        for (int i = 0; i < listOfFiles.length; i++) 
        {
            files = listOfFiles[i].getName();
            String curFile = new String(training_file_path + File.separator + files);
            
            String text = "";
            
            //Reads bytes of file and converts to string
            try
            {
            	text = new String(Files.readAllBytes(Paths.get(curFile)));
            }
            catch(IOException ex)
            {
            	ex.printStackTrace();
            }
            
            //Using the StanfordCoreNLP parser we obtain Lists containing tokens, POS tags, and sentences.
            List<String> tokens = getTokens(text);
            List<String> posTags = getPOSTags(text);
            List<String> sentences = getSentences(text);
            
            
            //Deals with scoring for number of sentences and length
            numOfSentences = sentences.size();
            aScore = getaScore(numOfSentences);
            
            System.out.println(listOfFiles[i].getName());
            System.out.println("This essay scored: " + aScore + " out of 5 for having " + numOfSentences + " sentences.");
            
            finalScore = (2 * aScore) - bScore + ciScore + ciiScore + (2 * ciiiScore) + (2 * diScore);
            
            p.printf(listOfFiles[i].getName()+ ";" + aScore + ";" + bScore + ";" + ciScore + ";" + ciiScore + ";" + ciiiScore + ";" + diScore + ";" + finalScore + ";" +  ranking + "\n");

            // TODO: add tokenizing to check length of essay, spellchecking, grammar checking, semantics(all as methods) and to implement a way to score them
            /*
            try {
                reader = new BufferedReader(new FileReader(curFile));
                String filecontents = null;
                while ((filecontents = reader.readLine()) != null){
                    System.out.println(filecontents);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
        }

        // TODO: replace with actual result writing implementation
        /*
        //write results.txt to the output folder using relative path
        BufferedWriter writer = null;
        String results_file_path = "../CS421Proj1/output/results.txt";
        System.out.println("Writing to results.txt file at relative path " + results_file_path);
        File resultsFile = new File(results_file_path);
        try {
            writer = new BufferedWriter(new FileWriter(resultsFile));
            String[] fileContents = new String[2];
            fileContents[0] = "12345.txt;1;2;4;1;0;0;0;5;low\n";
            fileContents[1] = "54321.txt;5;5;0;5;4;5;5;43;high";
            for (String content : fileContents) {
                writer.write(content);
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        
        p.close();
    }
}