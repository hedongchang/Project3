
public class Version1 extends Version {
	
	public static void calculatePopulation(int[] bounds, CensusData data, 
			CensusResult result, int x, int y) {	
		float latUnit = (result.latMax - result.latMin) / x;
		float longUnit = (result.longMax - result.longMin) / y;
		float southLat = (bounds[0] - 1) * latUnit + result.latMin;
		float northLat = bounds[1] * latUnit + result.latMin;
		float westLong = (bounds[2] - 1) * longUnit + result.longMin;
		float eastLong = bounds[3] * longUnit + result.longMin;
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
		report(groupPop, result.population);
	}
}
