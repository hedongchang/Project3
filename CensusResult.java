/**
 * A class to store the census result in US territory
 * 
 * @author Dongchang
 *
 */
public class CensusResult {
	public float latMin;
	public float latMax;
	public float longMin;
	public float longMax;
	public int population;
	
	public CensusResult(float latMin, float latMax, float longMin, float longMax, int population) {
		this.latMin = latMin;
		this.latMax = latMax;
		this.longMin = longMin;
		this.latMax = longMax;
		this.population = population;
	}
}
