import java.util.*;
import java.io.*;

public class MushroomAnalyzer 
{
	public static void main(String[] args) throws IOException{
		
	    	final boolean VERBOSE = false;
	    	
		Scanner fileScanner = new Scanner(new File("C:\\Users\\zayne\\eclipse-workspace\\ACM Research Test\\src\\mushrooms.csv"));
		MushroomAnalyzer mushroomAnalyzer = new MushroomAnalyzer();
		Mushroom[] shrooms = new Mushroom[10000];
		
		System.out.println("Parsing categories");
		String[] categories = fileScanner.next().split(",");
		
		System.out.println("Parsing mushroom data ");
		
		int numEdibleShrooms = 0;
		int numPoisonShrooms = 0;
		
		while(fileScanner.hasNext()){
			shrooms[Mushroom.numShrooms] = new Mushroom(fileScanner.next());
			
			if(shrooms[Mushroom.numShrooms-1].getProperty(0) == "e")
			    numEdibleShrooms++;
			else
			    numPoisonShrooms++;
		}
		
		System.out.println("Performing statistical analysis of property distribution");
		boolean matchFound = false;
		ArrayList<ArrayList<valueOccurence>> occurrences = new ArrayList<ArrayList<valueOccurence>>(23);
		
		//initializing outer ArrayList with empty ArrayList<valueOccurence>s
		for(int i = 0; i < 23; i++)
		    occurrences.add(new ArrayList<valueOccurence>(0));
		
		//Counts the number of occurrences of each recorded value of every property across every mushroom, counting separately by poison and edible
		//this is, strictly speaking O(n), but if the number of possible values for each property gets high it will be
		//a speed bottleneck
		for(int i = 0; i < Mushroom.numShrooms; i++) {
		    for(int j = 0; j < categories.length; j++) {
			matchFound = false;
			for(int k = 0; k < occurrences.get(j).size(); k++){
			    if(occurrences.get(j).get(k).value.equals(shrooms[i].getProperty(j))) {
				matchFound = true;
				if(VERBOSE) 
				    System.out.println("match found for property " + j + " value " + shrooms[i].getProperty(j) + " in shroom " + i);
				occurrences.get(j).get(k).addoccurrences((shrooms[i].getProperty(0)).equals("e"));
			    }
			}
			if(!matchFound) {
			    if(VERBOSE)
				System.out.println("NO MATCH FOUND for property " + j + " value " + shrooms[i].getProperty(j) + " in shroom " + i);
			    occurrences.get(j).add(mushroomAnalyzer.new valueOccurence(shrooms[i].getProperty(j), shrooms[i].getProperty(0).equals("e")));
			}
		    }
		}
		
		//show ratio of occurrences between edible/poison of every value of every property among all mushrooms
		System.out.println("******** RATIOS *********");
		for(int i = 1; i < categories.length; i ++) {
		    System.out.println(categories[i] + ":");
		    for(int j = 0; j < occurrences.get(i).size(); j++)
			    System.out.println("\t" + occurrences.get(i).get(j).getValue() + " : " + occurrences.get(i).get(j).edibleoccurrences + " to " + occurrences.get(i).get(j).poisonoccurrences + " : " + occurrences.get(i).get(j).getRatio());
		}
		
		int numCorrect = 0;
		int numCorrectOdor = 0;
		
		for(int i = 0; i < Mushroom.numShrooms; i++) {
		    float weight = 0;
		    for(int j = 1; j < categories.length; j++) {
			for(int k = 0; k < occurrences.get(j).size(); k++) {
			    if(occurrences.get(j).get(k).getValue().equals(shrooms[i].getProperty(j))){
				weight += (occurrences.get(j).get(k).getRatio());
				break;
			    }
			}
		    }
		    if(VERBOSE)
			System.out.println("Mushroom #" + i + " is predicted to be " + ((weight >= 0) ? "edible" : "poisonous") + ", and is " + shrooms[i].getProperty(0));
		    if(weight >= 0 && shrooms[i].getProperty(0).equals("e") || weight < 0 && shrooms[i].getProperty(0).equals("p"))
			numCorrect++;
		   
		    //alternative odor-based algorithm
		    char odor = shrooms[i].getProperty(5).charAt(0);
		    if(odor == 'a' || odor == 'l' || odor == 'n') {
			if(shrooms[i].getProperty(0).equals("e"))
			    numCorrectOdor++;
		    }
		    else {
			if(shrooms[i].getProperty(0).equals("p"))
			    numCorrectOdor++;
		    }
		}
		
		System.out.println("Summing the weight of all property values to reach a conclusion, we would predict edibility with the following success:");
		System.out.println(numCorrect + " predictions correct out of " + Mushroom.numShrooms + " mushrooms, for " + (numCorrect/(Mushroom.numShrooms+0.0)) + "% accuracy");
		System.out.println("This methodology is flexible, and for datasets in general could be a good approach. If the CSV file was changed, as long as the characteristics correlate, this should still work.");
		System.out.println("\nHowever, for this particular dataset, odor is the strongest single indicator;");
		System.out.println("odors 'a' and 'l' occur only for edible mushrooms, odors 'p','f', 'c', 'y', 's', and 'm' occur exclusively on poisonous mushrooms, ");
		System.out.println("and odor n, the most common odor, occurs 28 times more often for edible mushrooms than for poisonous ones (3408 to 120).");
		System.out.println("Using exclusively odor to make the determination, we would reach the following conclusions: ");
		System.out.println(numCorrectOdor + " predictions correct out of " + Mushroom.numShrooms + " mushrooms, for " + (numCorrectOdor/(Mushroom.numShrooms+0.0)) + "% accuracy");
}
	
	public class valueOccurence{
	    String value;
	    public int edibleoccurrences = 0;
	    public int poisonoccurrences = 0;
	    
	    public valueOccurence(String value, boolean edible) {
		this.value = value;
		this.addoccurrences(edible);
	    }
	    
	    public void addoccurrences(boolean edible) {
		if(edible)
		    edibleoccurrences++;
		else
		    poisonoccurrences++;
	    }
	    
	    public String getValue() {
		return value;
	    }
	    
	    public float getRatio() {
		if(edibleoccurrences == 0 && poisonoccurrences != 0)
		    return -20;
		if(edibleoccurrences != 0 && poisonoccurrences == 0)
		    return 20;
		if(edibleoccurrences > poisonoccurrences)   
		    return edibleoccurrences/poisonoccurrences;
		else
		    return -poisonoccurrences/edibleoccurrences;
	    }
	}
	
}
