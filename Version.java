public class Version {
	
	public static void report(int groupPop, int totalPop) {
		System.out.println("population of rectangle: " +  groupPop);
		System.out.printf ("percent of total population: %.2f", (float) 100 * groupPop / totalPop);
		System.out.println();
	}
}
