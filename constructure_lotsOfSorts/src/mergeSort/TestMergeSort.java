package mergeSort;

import java.util.ArrayList;

public class TestMergeSort {

	public static void main(String[] args) {
		int[] test = {8, 9, 10, 2, 5, 7, 9, 11, 13, 15, 6, 12, 14};
		nMergeSort(test, test.length);
		for (int i = 0; i < test.length; i++)
			System.out.print(test[i] + ", ");

	}
	
	public static void nMergeSort(int[] a, int n) {
		//tags:store index where the element is smaller than previous one.
		ArrayList<Integer> tags = new ArrayList<Integer>();
		
		//Add index 0 into tags.
		tags.add(0);
		//Find index.
		for (int i = 1; i < n; i++) {
			if (a[i] < a[i - 1])
				tags.add(i);
		}
		
		//extra: use to store sorted data
		int[] extra = new int[n];
		ArrayList<Integer> extraTags = new ArrayList<Integer>();
		while (tags.size() > 2) {
			nMergepass(a, extra, tags, extraTags, n);
			tags.clear();
			nMergepass(extra, a, extraTags, tags, n);
			extraTags.clear();
		}
	}
	
	public static void nMergepass(int[] ini, int[] result, ArrayList<Integer> tags, ArrayList<Integer> extraTags, int n) {
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
	
	public static void nMerge(int[] ini, int[] result, int start, int middle, int end, ArrayList<Integer> extraTags) {
		int j = middle, k = start, i = start;
		extraTags.add(start);
		while (i < middle && j <= end) {
			if (ini[i] <= ini[j])
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

}
