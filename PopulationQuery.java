
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PopulationQuery {
	// next four constants are relevant to parsing
	public static final int TOKENS_PER_LINE  = 7;
	public static final int POPULATION_INDEX = 4; // zero-based indices
	public static final int LATITUDE_INDEX   = 5;
	public static final int LONGITUDE_INDEX  = 6;
	
	// parse the input file into a large array held in a CensusData object
	public static CensusData parse(String filename) {
		CensusData result = new CensusData();
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(filename));
            
            // Skip the first line of the file
            // After that each line has 7 comma-separated numbers (see constants above)
            // We want to skip the first 4, the 5th is the population (an int)
            // and the 6th and 7th are latitude and longitude (floats)
            // If the population is 0, then the line has latitude and longitude of +.,-.
            // which cannot be parsed as floats, so that's a special case
            //   (we could fix this, but noisy data is a fact of life, more fun
            //    to process the real data as provided by the government)
            
            String oneLine = fileIn.readLine(); // skip the first line
            // read each subsequent line and add relevant data to a big array
            while ((oneLine = fileIn.readLine()) != null) {
                String[] tokens = oneLine.split(",");
                if(tokens.length != TOKENS_PER_LINE)
                	throw new NumberFormatException();
                int population = Integer.parseInt(tokens[POPULATION_INDEX]);
                if(population != 0)
                	result.add(population,
                			   Float.parseFloat(tokens[LATITUDE_INDEX]),
                		       Float.parseFloat(tokens[LONGITUDE_INDEX]));
            }

            fileIn.close();
        } catch(IOException ioe) {
            System.err.println("Error opening/reading/writing input or output file.");
            System.exit(1);
        } catch(NumberFormatException nfe) {
            System.err.println(nfe.toString());
            System.err.println("Error in file format");
            System.exit(1);
        }
        return result;
	}

	// argument 1: file name for input data: pass this to parse
	// argument 2: number of x-dimension buckets
	// argument 3: number of y-dimension buckets
	// argument 4: -v1, -v2, -v3, -v4, or -v5
	public static void main(String[] args) {
		if (args.length != 4) {
			System.err.println("Incorrect number of arguments");
			System.exit(1);
		}
		String fileName = args[0];
		CensusData data = parse(fileName);
		int x = Integer.parseInt(args[1]);
		int y = Integer.parseInt(args[2]);
		int version = Integer.parseInt(args[3]);
		int[] bounds = askForInput(x, y);
        CensusResult result = getCorner(data);
		if (version == 1) {
			Version1.calculatePopulation(bounds, data, result, x, y);
		} else if (version == 2) {
			Version2.calcualtePopulation(bounds, data, result, x, y);
		}
	}
	
	private static CensusResult getCorner(CensusData data) {
		// latMin latMax longMin longMax
		CensusGroup first = data.data[0];
		CensusResult result = new CensusResult(first.latitude, first.latitude, 
				first.longitude, first.longitude, 0);
		for (int i = 0; i < data.data_size; i++) {
			float latitude = data.data[i].latitude;
			float longitude = data.data[i].longitude;
			result.latMin = Math.min(result.latMin, latitude);
			result.latMax = Math.max(result.latMax, latitude);
			result.longMin = Math.min(result.longMin, longitude);
			result.longMax = Math.max(result.longMax, longitude);
			result.population = result.population + data.data[i].population;
		}
		return result;
	}
	
	private static int[] askForInput(int x, int y) {
		Scanner input = new Scanner(System.in);
		int i = 0;
		int[] bounds = new int[4];
		System.out.println("Give bounds of a census group (south, north, west, east)");
		while (i < bounds.length && input.hasNextInt()) {
			int inputNum = input.nextInt();
			if (i == 0) {
				if (inputNum < 1 || inputNum > y) {
					System.err.println("wrong bound");
				} 
			} else if (i == 1) {
				if (inputNum < bounds[0] || inputNum > y) {
					System.err.println("wrong bound");
				}
			} else if (i == 2) {
				if (inputNum < 1 || inputNum > x) {
					System.err.println("wrong bound");
				}
			} else {
				if (inputNum < bounds[2] || inputNum > x) {
					System.err.println("wrong bound");
				}
			}
			bounds[i] = inputNum;
			i++;
		}
		if (i != 3) {
			System.err.println("Incorrect number of bounds");
			System.exit(1);
		}
		input.close();
		return bounds;
	}
	
	public static void preprocess(String file, int row, int column, int version) {
		
	}
	
	public static Pair<Integer, Double> singleInteraction(int w, int s, int e, int n) {
		return new Pair<Integer, Double>(1, 0.1);
	}
}
