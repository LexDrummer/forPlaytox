import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountTread extends Thread {
    private static final Logger logger = LogManager.getLogger(AccountTread.class);


    @Override
    public void run() {
            //условие завершения работы потока, необходимое колличество транзакций
            while (Main.getCounter() < 30) {
                Main.incrementCounter();
                Main.transaction();
                try {
                    Thread.sleep((long)((Math.random() * 1000) + 1000));
                } catch (InterruptedException e) {
                    logger.error("You have Interrupted Exception - " + e.getMessage());
                }
            }


    }
}