package array;

public class ArrayReturnExam01 {
    public static int[] addOne(int[] intArr) {
        int[] resultArr = new int[intArr.length];

        for (int i = 0; i < intArr.length; i++) {
            resultArr[i] = intArr[i] + 1;
        }

        return resultArr;
    }

    public static void main(String[] args) {
        int[] intArray = {1, 2, 3, 4};
        
        int[] intArr2 = addOne(intArray);

        System.out.print("원본 배열: ");
        for (int num : intArray) {
            System.out.print(num + " ");
        }
        System.out.println();

        System.out.print("결과 배열: ");
        for (int num : intArr2) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}