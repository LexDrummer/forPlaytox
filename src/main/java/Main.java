import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/*
Изначально, читая в ТЗ строчки "Решение должно обеспечивать возможность одновременного (параллельного)
перевода средств со счета a1 на счет a2 и со счета a3 на счет а4 в разных потоках.", я подумал,
что мне необходимо синхронизироваться сразу на двух объектах. При этом два объекта выбираются случайно
из существующих. При таком подходе я не нашел элегантного решения, потому что состояние гонки или deadlock
могли нарушить ход программы (Если что, я процентов на 80 уверен, что так сделать можно, просто мне не
хватило опыта или знаний или мозгов).
Поэтому решение пошло по пути: одна операция на одном счету - одна блокировка на этом объекте.
 */

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static volatile int counter = 0;
    public static List<Account> accounts = new ArrayList<>();
    public static final Object lock = new Object();

    public static int getCounter() {
        synchronized (lock) {
            return counter;
        }
    }

    public static void incrementCounter(){
        synchronized (lock) {
            counter++;
        }
    }

    private static Account getRandomAcc () {
        return accounts.get((int) (Math.random() * accounts.size()));
    }

    public static void transaction(){
        Account accFrom;
        Account accTo;
        long amount;
        synchronized (accFrom = getRandomAcc()) {
            long balanceFromBefore = accFrom.getMoney();

            /*
            Не совсем понято условие без уточнений "Сумма списания или зачисления определяется
            случайным образом.". Я предположил, что логично оставить верхнюю границу случайной суммы
            - балансом на счету. С другой стороны можно ограничить перевод 10000, но поставить проверку
            на наличие средств на счету и не производить операцию. Выбрал первый вариант, как более логичный,
            на мой взгляд.
             */
            amount = (long) (Math.random() * accFrom.getMoney());

            accFrom.setMoney(accFrom.getMoney() - amount);
            logger.info("From " + accFrom.getID() + " (" + balanceFromBefore + ") was withdrawn " + amount
                + ". Now balance of " + accFrom.getID() + " is - " + accFrom.getMoney());
        }
        synchronized (accTo = getRandomAcc()) {
            while (accFrom.equals(accTo) && accounts.size() > 1) {
                accTo = getRandomAcc();
            }
            long balanceToBefore = accTo.getMoney();
            accTo.setMoney(accTo.getMoney() + amount);
                logger.info("From account " + accFrom.getID() + " to " + accTo.getID() + " (" +
                        balanceToBefore + ") was deposit " + amount + ". Now balance of " +
                        accTo.getID() + " is - " + accTo.getMoney());
            }
    }

    public static void main(String[] args) {
        // создаем необходимое колличество аккаунтов, добавляем их в Лист для рандомной выборки
        for (int i = 0; i < 4; i++) {
            accounts.add(new Account());
        }
        // создаем и запускаем необходимое колличество потоков
        for (int i = 0; i < 2; i++) {
            new AccountTread().start();
        }

    }
}
