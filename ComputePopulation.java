import java.util.concurrent.RecursiveTask;


public class ComputePopulation extends RecursiveTask<Integer>{
	private static final long serialVersionUID = 1L;

	public static final int SEQUENTIAL_CUTOFF = 100;
	
	private int lo;
	private int hi;
	private int x;
	private int y;
	private int[] bounds;
	private CensusData data;
	private CensusResult result;
	
	public ComputePopulation(int lo, int hi, int x, int y, int[] bounds, CensusData data, CensusResult result) {
		this.lo = lo;
		this.hi = hi;
		this.x = x;
		this.y = y;
		this.bounds = bounds;
		this.data = data;
		this.result = result;
	}
	
	protected Integer compute() {
		float latUnit = (result.latMax - result.latMin) / x;
		float longUnit = (result.longMax - result.longMin) / y;
		float southLat = (bounds[0] - 1) * latUnit + result.latMin;
		float northLat = bounds[1] * latUnit + result.latMin;
		float westLong = (bounds[2] - 1) * longUnit + result.longMin;
		float eastLong = bounds[3] * longUnit + result.longMin;
		if (hi - lo <= SEQUENTIAL_CUTOFF) {
			int groupPop = 0;
			for (int i = 0; i < data.data_size; i++) {
				if (data.data[i] != null) {
					CensusGroup group = data.data[i];
					float groupLat = group.latitude;
					float groupLong = group.longitude;
					if (groupLat >= southLat && groupLat <= northLat && 
							groupLong >= westLong && groupLat <= eastLong) {
						groupPop = groupPop + data.data[i].population;
					}
				}
			}
			return groupPop;
		} else {
			ComputePopulation left = new ComputePopulation(lo, (hi + lo) / 2, x, y, bounds, data, result);
			ComputePopulation right = new ComputePopulation(lo, (hi + lo) / 2, x, y, bounds, data, result);
			left.fork();
			int rightAns = right.compute();
			int leftAns = left.join();
			return leftAns + rightAns;
		}
	}
}
