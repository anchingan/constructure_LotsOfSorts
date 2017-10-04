package heapSort;

public class TestHeapSort {

	public static void main(String[] args) {
		int[] test= {5, 19, 10, 2, 1, 3, 2, 7, 2};
		for (int i = 0; i < test.length; i++) 
			System.out.print(test[i] + ", ");
		heapSort(test, test.length);
		System.out.println("\nAfter heap sort:");
		for (int i = 0; i < test.length; i++) 
			System.out.print(test[i] + ", ");
	}
	
	public static void heapSort(int[] data, int number) {
		int i;
		for (i = (number - 2) / 2; i >= 0; i--)
			adjust(data, i, number);
		
		for(i = number - 2; i >= 0; i--) {
			swap(data, 0, i + 1);
			adjust(data, 0, i + 1);
		}
	}
	
	//Adjust as a max heap.
	public static void adjust(int[] data, int root, int number) {
		int child = root * 2 + 1; //left child
		int temp = data[root];
		while (child < number) {
			if ((child < number - 1) && data[child] < data[child + 1])
				child++;
			if (temp > data[child])
				break;
			else {
				data[(child - 1)/2] = data[child]; //set current parent as this child;
				child = child * 2 + 1;
			}
		}
		data[(child - 1) / 2] = temp;
	}
	
	public static void swap(int[] data, int s1, int s2) {
		int temp = data[s1];
		data[s1] = data[s2];
		data[s2] = temp;
	}

}
