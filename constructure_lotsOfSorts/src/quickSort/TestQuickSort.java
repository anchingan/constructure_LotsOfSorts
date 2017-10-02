package quickSort;

public class TestQuickSort {

	public static void main(String[] args) {
		int[] test= {5, 19, 10, 2, 1, 3, 2, 7, 2};
		for (int i = 0; i < test.length; i++) 
			System.out.print(test[i] + ", ");
		quickSort(test, 0, test.length - 1);
		System.out.println("\nAfter quicksort:");
		for (int i = 0; i < test.length; i++) 
			System.out.print(test[i] + ", ");
	}
	
	public static void quickSort(int[] data, int start, int end) {
		if (start < end) {
			int pivot = data[start], i = start + 1, j = end;
			int temp;
			while(i < j) {
				if (data[i] > pivot && data[j] <= pivot) {
					temp = data[i];
					data[i] = data[j];
					data[j] = temp;
					i++;
					j--;
				}
				while (data[i] <= pivot && i < end)
					i++;
				while (data[j] > pivot)
					j--;
			}
			data[start] = data[j];
			data[j] = pivot;
			quickSort(data, start, j - 1);
			quickSort(data, j + 1, end);
		}
	}

}
