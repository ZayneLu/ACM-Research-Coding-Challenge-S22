public class Mushroom{
    public static int numShrooms = 0;
    private int mushroomNumber;
    private String[] properties;
    private String[] categories = {"class",
	    			  "cap shape", "cap surface", "cap color", 
	    			  "bruises", "odor", 
	    			  "gill attachment", "gill spacing", "gill size", "gill color",
	    			  "stalk shape", "stalk root", "stalk surface above ring", "stalk surface below ring", "stalk color above ring", "stalk color below ring",
	    			  "veil type", "veil color", 
	    			  "ring number", "ring type", 
	    			  "spore print color", "population", "habitat" };
    
    
    public Mushroom(String csv) {
	properties = csv.split(",");
	mushroomNumber = numShrooms++;
    }
    
    public String getProperty(int n) {
	if(n < 23)
	    return properties[n];
	return null;
    }
    
    
    //returns string of properties all properties
    public String getInfo() {
	StringBuilder info = new StringBuilder("******** Mushroom number: " + mushroomNumber + " ********\n");
	for(int i = 0; i < properties.length; i++)
	    info.append(String.format("%-24s",categories[i]) + ": " + properties[i] + "\n");
	return info.toString();
    }
    
}
