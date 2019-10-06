import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class Pizza {
    Pizza(){}
	public static class PizzaShop {
        private final Lock lock = new ReentrantLock();
	    private final Condition canBuyLargePizzas = lock.newCondition();
        private final Condition canBuySmallPizzas = lock.newCondition();
        private int largePizzas = 0;
        private int smallPizzas = 0;

        PizzaShop(){}

		public void buyLargePizza() {
            lock.lock();
            try{
                while(largePizzas == 0 || smallPizzas < 2){
                    System.out.println(Thread.currentThread().getName()+"-> buyLargePizza -> waiting");
                    System.out.println("State: large "+largePizzas+",small "+smallPizzas);
                    canBuyLargePizzas.await();
                }

                    System.out.println(Thread.currentThread().getName()+"->buyLargePizza->buying");
                    System.out.println("State: large "+largePizzas+",small "+smallPizzas);

                if(largePizzas == 0){
                    largePizzas--;
                }
                else{
                    smallPizzas -= 2;
                }
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally{
                lock.unlock();
            }
		}
		
		
		public void buySmallPizza() {

	        lock.lock();
            try{
                while(smallPizzas == 2){
                    System.out.println(Thread.currentThread().getName()+"-> buySmallPizza -> waiting");
                    System.out.println("State: large "+largePizzas+",small "+smallPizzas);
                    canBuySmallPizzas.await();
                }
                    System.out.println(Thread.currentThread().getName()+"-> buySmallPizza -> buying");
                    System.out.println("State: large "+largePizzas+",small "+smallPizzas);

                smallPizzas--;
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally{
                lock.unlock();
            }
		
		}

		public void bakeLargePizza() {
		    lock.lock();
            largePizzas++;
            canBuyLargePizzas.signal();
            lock.unlock();
		
		}

		public void bakeSmallPizza() {
    		lock.lock();
            smallPizzas++;
            if(smallPizzas >= 2){
                canBuyLargePizzas.signal();
            }
            canBuySmallPizzas.signal();
            lock.unlock();
                
		}

	}
	
	public static class Agent implements Runnable {
		private int type;
		private PizzaShop ps;
		
		Agent(int type, PizzaShop ps) {
			this.type=type;
			this.ps=ps;
		}
		
		public void run() {
			try {
				Thread.sleep(new Random().nextInt(1500));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (type) {
			case 0 : 
				System.out.println(Thread.currentThread().getName()+"-> buyLargePizza");
				ps.buyLargePizza();
				break;
			case 1: 
				System.out.println(Thread.currentThread().getName()+"-> buySmallPizza");
				ps.buySmallPizza();
				break;
			case 2 : 
				System.out.println(Thread.currentThread().getName()+"-> bakeLargePizza");
				ps.bakeLargePizza();
				break;
			default: 
				System.out.println(Thread.currentThread().getName()+"-> bakeSmallPizza");
				ps.bakeSmallPizza();
				break;
			}
		}
	}
	
    public static void main(String[] args) {
    	PizzaShop ps = new PizzaShop();
    	final int N=1000;
    	Random r = new Random();
    	// 0 -> buyLargePizza
    	// 1 -> buySmallPizza
    	// 2 -> bakeLargePizza
    	// 3 -> bakeSmallPizza
    	
    
    	for (int i=0; i<N; i++) {
    		new Thread(new Agent(r.nextInt(4),ps)).start();
    	}			

    }
}


