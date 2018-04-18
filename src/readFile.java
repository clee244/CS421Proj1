import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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


public class readFile {

    public static void main(String[] args) {
        //Load test.txt from the resources folder using relative path
        BufferedReader reader = null;
        String files;

        String training_file_path = "../CS421Proj1/input/training/essays/";
        System.out.println("Opening a .txt at relative path " + training_file_path);
        File textFile = new File(training_file_path);
        File[] listOfFiles = textFile.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            files = listOfFiles[i].getName();

            File curFile = new File(training_file_path + File.separator + files);

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
    }
}
