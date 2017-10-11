package externalSort;

import java.io.*;
import java.util.ArrayList;

public class TestExternalSort {
	static String title;

	public static void main(String[] args) throws IOException {
		Stopwatch timer = new Stopwatch();
		timer.start();
		int fileNum = readAndSplit();
		sortAndMerge(fileNum);
		
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
	
	public static int readAndSplit() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("timetable.csv"));
		title = br.readLine();
		String str;
		String[] strs;
		int[][] times; //[][0]:time, [][1]:index;
		int i = 0;
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
			wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test" + (i + 1) + ".csv")));
			for (int j = 0; j < n; j++) {
				wr.write(strs[times[j][1]]);
				wr.newLine();
			}
			wr.flush();
			wr.close();
			i++;
		}
		br.close();
		return i;
	}
	
	//Adjust as a min heap
	public static void adjust(ExternalSort[] data, int root, int number) {
		int child = root * 2 + 1; //left child
		ExternalSort temp = data[root];
		while (child <= number) {
			if ((child <= number - 1) && data[child].time > data[child + 1].time)
				child++;
			if (temp.time < data[child].time)
				break;
			else {
				data[(child - 1)/2] = data[child]; //set current parent as this child;
				child = child * 2 + 1;
			}
		}
		data[(child - 1) / 2] = temp;
	}
	
	public static void buildMinHeapTree(ExternalSort[] data, int number) {
		int i = number - 1;
		for (i = (i - 1) / 2; i >= 0; i--)
			adjust(data, i, number);
	}
	
	public static void swap(ExternalSort[] data, int index1, int index2) {
		ExternalSort temp = data[index1];
		data[index1] = data[index2];
		data[index2] = temp;
	}
	
	//read data from split file.
	public static void readFromSplit(BufferedReader[] bufferedFiles, String[][] strs, int[][] values, int bufferLength, int index) throws IOException {
		String str;
		if (bufferedFiles[index].ready()) {
			for (int j = 0; j < bufferLength; j++) {
				if ((str = bufferedFiles[index].readLine()) != null) {
					strs[index][j] = str;
					values[index][j] = Integer.parseInt(str.split(",")[7]);
				}
				if (str == null) {
					strs[index][j] = null;
					values[index][j] = -1;
					bufferedFiles[index].close();
					break;
				}
			}
		}
	}
	
	public static void sortAndMerge(int fileNum) throws IOException {
		//Read first 50 data from all split files.
		int bufferLength = 50;
		BufferedReader[] bufferedFiles = new BufferedReader[fileNum];
		String[][] strs = new String[fileNum][bufferLength];
		int[][] values = new int[fileNum][bufferLength];
		String str;
		
		for (int i = 0; i < fileNum; i++) {
			bufferedFiles[i] = new BufferedReader(new FileReader("test" + (i + 1) + ".csv"));
			readFromSplit(bufferedFiles, strs, values, bufferLength, i);
		}
		
		ExternalSort[] toHeapSort = new ExternalSort[fileNum];
		//write first data from each buffer file to ExternalSort[] toHeapSort.
		for (int i = 0; i < fileNum; i++)
			toHeapSort[i] = new ExternalSort(i, values[i][0], strs[i][0], 0);
		//adjust as a min heaptree
		buildMinHeapTree(toHeapSort, fileNum);
		BufferedWriter bw = new BufferedWriter(new FileWriter("sortresult.csv"));
		//write title to file.
		bw.write(title);
		bw.newLine();
		int num = fileNum - 1; //record last index in heaptree
		int total = 0;
		while (num > 0) {
			bw.write(toHeapSort[0].data);
			bw.newLine();
			//replace toHeapSort[0]
			if (toHeapSort[0].index >= bufferLength - 1) {
				if (!bufferedFiles[toHeapSort[0].origin].ready()) 
					swap(toHeapSort, 0, num--);
				else {
					readFromSplit(bufferedFiles, strs, values, bufferLength, toHeapSort[0].origin);
					toHeapSort[0].index = 0;
					toHeapSort[0].data = strs[toHeapSort[0].origin][0];
					toHeapSort[0].time = values[toHeapSort[0].origin][0];
				}
			}
			else {
				toHeapSort[0].index++;
				if (values[toHeapSort[0].origin][toHeapSort[0].index] == -1)
					swap(toHeapSort, 0, num--);
				else {
					toHeapSort[0].data = strs[toHeapSort[0].origin][toHeapSort[0].index];
					toHeapSort[0].time = values[toHeapSort[0].origin][toHeapSort[0].index];
				}
			}
			adjust(toHeapSort, 0, num);
		}
		bw.write(toHeapSort[0].data);
		total++;
		bw.newLine();
		bw.write(toHeapSort[1].data);
		total++;
		bw.flush();
		bw.close();
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

