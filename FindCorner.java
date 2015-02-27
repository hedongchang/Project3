import java.util.concurrent.RecursiveTask;


public class FindCorner extends RecursiveTask<CensusResult> {
	private static final long serialVersionUID = 1L;

	public static final int SEQUENTIAL_CUTOFF = 100;
	
	private int lo;
	private int hi;
	private CensusData data;
	
	public FindCorner(int hi, int lo, CensusData data) {
		this.lo = lo;
		this.hi = hi;
		this.data = data;
	}
	
	protected CensusResult compute() {
		if (hi - lo <= SEQUENTIAL_CUTOFF) {
			CensusGroup first = data.data[0];
			CensusResult result = new CensusResult(first.latitude, first.latitude, 
					first.longitude, first.longitude, 0);
			for (int i = lo; i < hi; i++) {
				float latitude = data.data[i].latitude;
				float longitude = data.data[i].longitude;
				result.latMin = Math.min(result.latMin, latitude);
				result.latMax = Math.max(result.latMax, latitude);
				result.longMin = Math.min(result.longMin, longitude);
				result.longMax = Math.max(result.longMax, longitude);
				result.population = result.population + data.data[i].population;
			}
			return result;
		} else {
			FindCorner left = new FindCorner(lo, (hi + lo) / 2, data);
			FindCorner right = new FindCorner((hi + lo) / 2, hi, data);
			left.fork();
			CensusResult rightAns = right.compute();
			CensusResult leftAns = left.join();
			float latMin = Math.min(leftAns.latMin, rightAns.latMin);
			float latMax = Math.max(leftAns.latMax, rightAns.latMax);
			float longMin = Math.min(leftAns.longMin, rightAns.longMin);
			float longMax = Math.max(leftAns.longMax, rightAns.longMax);
			int population = leftAns.population + rightAns.population;
			return new CensusResult(latMin, latMax, longMin, longMax, population);
		}
	}
}
