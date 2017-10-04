package externalSort;

import java.io.*;
import java.util.ArrayList;

public class TestExternalSort {

	public static void main(String[] args) throws IOException {
		Stopwatch timer = new Stopwatch();
		timer.start();
		readAndSplit();
		
		timer.stop();
		System.out.println(timer.getTotalTime());
		System.out.println("Finished!");

	}
	
	public static void nMergeSort(int[][] a, int n) {
		//tags:store index where the element is smaller than previous one.
		ArrayList<Integer> tags = new ArrayList<Integer>();
		
		//Add index 0 into tags.
		tags.add(0);
		//Find index.
		for (int i = 1; i < n; i++) {
			if (a[i][0] < a[i - 1][0])
				tags.add(i);
		}
		
		//extra: use to store sorted data
		int[][] extra = new int[n][2];
		ArrayList<Integer> extraTags = new ArrayList<Integer>();
		while (tags.size() > 1) {
			nMergepass(a, extra, tags, extraTags, n);
			tags.clear();
			nMergepass(extra, a, extraTags, tags, n);
			extraTags.clear();
		}
	}
	
	public static void nMergepass(int[][] ini, int[][] result, ArrayList<Integer> tags, ArrayList<Integer> extraTags, int n) {
		//n: total numbers need to sort
		int i = 0;
		for (; i < tags.size() - 2; i += 2) {
			nMerge(ini, result, tags.get(i), tags.get(i + 1), tags.get(i + 2) - 1, extraTags);
		}
		if (tags.size() - i == 2) 
			nMerge(ini, result, tags.get(i), tags.get(i + 1), n - 1, extraTags);
		else if (i < tags.size()) {
			int k = tags.get(i);
			extraTags.add(k);
			for (; k < n; k++)
				result[k] = ini[k];
		}
	}
	
	public static void nMerge(int[][] ini, int[][] result, int start, int middle, int end, ArrayList<Integer> extraTags) {
		int j = middle, k = start, i = start;
		extraTags.add(start);
		while (i < middle && j <= end) {
			if (ini[i][0] <= ini[j][0])
				result[k++] = ini[i++];
			else
				result[k++] = ini[j++];
		}
		if (i < middle) {
			for (; i < middle; i++)
				result[k++] = ini[i];
		}
		else {
			for (; j <= end; j++)
				result[k++] = ini[j];
		}
	}
	
	public static void readAndSplit() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("timetable.csv"));
		String title = br.readLine();
		String str;
		String[] strs;
		int[][] times; //[][0]:time, [][1]:index;
		int i = 1;
		BufferedWriter wr;
		int n, length = 2500;
		while ((str = br.readLine()) != null) {
			n = 0;
			strs = new String[length];
			times = new int[length][2];
			strs[n] = str;
			times[n][0] = Integer.parseInt(str.split(",")[7]);
			times[n][1] = n++;
			while (n < length && (str = br.readLine()) != null) {
				strs[n] = str;
				times[n][0] = Integer.parseInt(str.split(",")[7]);
				times[n][1] = n++;
			}
			nMergeSort(times, n);
			wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test" + i + ".csv")));
			for (int j = 0; j < n; j++) {
				wr.write(strs[times[j][1]]);
				wr.newLine();
			}
			wr.flush();
			wr.close();
			i++;
		}
		br.close();
	}
}

class Stopwatch{
	private long start,end;
	public Stopwatch(){
		reset();
	}
	public void reset(){
		start = 0;
		end = 0;
	}
	public void start(){
		start = System.currentTimeMillis();
	}
	public void stop(){
		end = System.currentTimeMillis();
	}
	public long getTotalTime(){
		return end-start;
	}
} 

