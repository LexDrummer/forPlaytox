
public class Account {
    private final String ID;
    /*
    я подумал использовать здесь объект класса BigDecimal, который
     чаще используют при операциях связанных с деньгами, но в ТЗ четко
     указано использовать целочисленную переменную. Поэтому будет long (с учетом масштабируемости
     приложения).
     */
    private long money;

    public Account() {
        this.ID = getRandomAccountId();
        this.money = 10000L;
    }

    public String getID() {
        return ID;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        /*
        после написания рабочего прототипа программы, подумал, что логику работы логгера можно
        прописать здесь, ведь в задании сказано логгировать любые изменения данных на счету.
        правда в классе Main можно получить больше информации для лога, поэтому рабочий вариант:
        логгирование из класса Main
         */
        this.money = money;
    }

    // метод для генерации псевдослучайной последовательности цифр, больших и маленьких букв в поле ID.
    private static String getRandomAccountId(){

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            if(Math.random() < 0.5) {
                stringBuilder.append((int) (Math.random() * 10));
            }
            else {
                if(Math.random() < 0.5)
                    //случайная маленькая буква
                    stringBuilder.append( (char) (97 + Math.random() * 26));
                else
                    //случайная большая буква
                    stringBuilder.append( (char) (65 + Math.random() * 26));
            }

        }
        return stringBuilder.toString();
    }

}
