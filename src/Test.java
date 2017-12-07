
public class Test {

	public static void main(String[] args) {
		for (int i = 0 ; i < 10; i++)  {
			try {
				System.out.println("Hello GitHub " + i + ", this is my first test");
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
