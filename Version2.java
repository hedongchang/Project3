import java.util.concurrent.ForkJoinPool;


public class Version2 extends Version {
	
    public static ForkJoinPool fjPool = new ForkJoinPool();

	public static void calcualtePopulation(int[] bounds,  CensusData data, int x, int y) {
		CensusResult result = fjPool.invoke(new FindCorner(0, data.data_size, data));
		int groupPop = fjPool.invoke(new ComputePopulation(0, data.data_size, x, y, bounds, data, result));
		report(groupPop, result.population);
	}
}
