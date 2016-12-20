package dynamicconnectivity;

import java.util.Scanner;

public class QuickFindUFClient {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		System.out.print("Enter N - number of objects: ");
		int N = in.nextInt();
		QuickFindUF uf = new QuickFindUF(N);
		
		String input_pair = "";
		while(input_pair != "x") {
			String[] arr_input_pair = new String[0];
			System.out.print("Enter p,q pair e.g. 1,2. Enter x to terminate: ");
			input_pair = in.next();
			if (input_pair.equals("x")) {
				System.out.println("OVER");
				System.exit(0);
			}
			else if (!input_pair.equals("x")) {
				arr_input_pair = input_pair.split(",");
				int p = Integer.parseInt(arr_input_pair[0]);
				int q = Integer.parseInt(arr_input_pair[1]);
				
				if(!uf.connected(p, q)) {
					uf.union(p, q);
					System.out.println("Connected " + p + " and " + q);
				}
				else {
					System.out.println("Already connected " + p + " and " + q);
				}
			}
		}
		in.close();
	}
}
