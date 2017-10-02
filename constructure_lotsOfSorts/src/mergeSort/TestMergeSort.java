package mergeSort;

public class TestMergeSort {

	public static void main(String[] args) {
		int[] test= {5, 19, 10, 2, 1, 3, 2, 7, 2};
		for (int i = 0; i < test.length; i++) 
			System.out.print(test[i] + ", ");
		reMergeSort(test, 0, test.length - 1);
		System.out.println("\nAfter mergesort:");
		for (int i = 0; i < test.length; i++) 
			System.out.print(test[i] + ", ");

	}
	
	public static void reMergeSort(int[] ini, int start, int end) {
		if (start < end) {
			int middle = (start + end) / 2;
			reMergeSort(ini, start, middle);
			reMergeSort(ini, middle + 1, end);
			reMerge(ini, start, middle, end);
		}
	}
	
	public static void reMerge(int[] ini, int start, int middle, int end) {
		int[] temp = new int[end - start + 1];
		int i = start, j = middle + 1, k = 0;
		while (i <= middle && j <= end) {
			if (ini[i] <= ini [j])
				temp[k++] = ini[i++];
			else
				temp[k++] = ini[j++];
		}
		if (i <= middle) {
			for (; i <= middle; i++, k++)
				temp[k] = ini[i];
		}
		else {
			for (; j <= end; j++, k++)
				temp[k] = ini[j];
		}
		
		for (i = start, k = 0; i <= end; i++, k++)
			ini[i] = temp[k];
			
	}
	
	public static void mergeSort(int[] data, int number) {
		int[] extra = new int[data.length];
		int s = 1;
		
		while (s < number) {
			mergePass(data, extra, s, number);
			s *= 2;
			mergePass(extra, data, s, number);
		}
	}
	
	public static void mergePass(int[] ini, int[] result, int s, int number) {
		int i = 0;
		for (; i <= number - s * 2; i += s * 2)
			merge(ini, result, i, i + s * 2 - 1, s);
		if (i <= number - s)
			merge(ini, result, i, number - 1, s);
		else 
			for (; i < number; i++)
				result[i] = ini[i];
	}
	
	public static void merge(int[] ini, int[] result, int start, int end, int s) {
		int i = start, k = start, j = start + s;
		while (i < start + s && j <= end) {
			if (ini[i] <= ini[j])
				result[k++] = ini[i++];
			else
				result[k++] = ini[j++];
		}
		
		if (i < start + s) {
			for (; k <= end; k++, i++)
				result[k] = ini[i];
		}
		else {
			for (; k <= end; k++, j++)
				result[k] = ini[j];
		}
	}

}
