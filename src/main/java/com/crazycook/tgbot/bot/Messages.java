package com.crazycook.tgbot.bot;

public class Messages {
    public static final String BOLD_START = "<b>";
    public static final String BOLD_END = "</b>";
    public static final String LINE_END = "\n";
    public static final String VALUE = "%s";
    public static final String POINT = ".";
    public static final String ONE_SPACE = " ";
    public static final String FOUR_SPACES = "    ";

    public static final String RED_DIAMOND = "♦️";
    public static final String BLUE_DIAMOND = "\uD83D\uDD39";
    public static final String YELLOW_DIAMOND = "\uD83D\uDD38";

    public static final String IN_YOUR_CART = "В твоїй корзині зараз: \n";
    public static final String YOUR_CART_IS_EMPTY = "\uD83D\uDE32 Тут пуст! Жодного макарончика \uD83E\uDD37\u200D♂️";
    public static final String COMMENT = "Коментар: ";
    public final static String ADDRESS_SAVED = "Адресу збережено. ";
    public final static String ADDRESS = BOLD_START + "Адреса доставки: " + BOLD_END;
    public static final String REQUEST_CONTACT = "Будь ласка поділіться своїм контактом, щоб ми могли звязатися з вами для уточнення деталей доставки";
    public static final String DELIVERY_METHOD = "Cпосіб доставки: ";

    public static final String NEGATIVE_NUMBER_REACTION = "\uD83E\uDD28 Серйозно, від'ємне значення? Ти, що тестувальник(ця) \uD83D\uDE01? \nЯкщо в тебе є бажання допомогти нам тестувати цей бот, звернись до наших адмінів.";
    public static final String FLOATING_NUMBER_REACTION = "\uD83E\uDDD0 Це що дробне значенн? Ми продаємо лише цілі бокси і цілі макарони.";
    public static final String TO_LONG_INTEGER_NUMBER_REACTION = "\uD83D\uDE02 Ого, оце ти ласун(ка)! Але в нас стільки макаронів немає, спробуй написати меншу кількість.";

    public static final String LEAVE_COMMENT = "Якщо хочешь, можешь залишити коментар до замовлення. Наприклад, можешь вказати як краще з тобою звязуватись - писати в телеграмі, чи дзвонити на телефон. Можешь написати побажання до дати і часу доставки, або будь яке інше побажання.";
    public static final String WAITING_COMMENT = "\uD83E\uDDDE\u200D♂️ Я слухаю, пиши.";
    public static final String COMMENT_ADDED = "Дякую, коментар додали.";

    public static final String THANKS_MESSAGE = "Дякуємо за замовлення, наш менеджер скоро звяжеться з вами.";

    public final static String FLAVOR_MESSAGE = BOLD_START + "Зараз в наявності є такі смаки: " + BOLD_END + LINE_END;

    public final static String CART_WAS_REFRESH = "Корзину очищено, зараз в ній нічого немає.";

    public final static String DELIVERY_MESSAGE = """
            Доставка можлива тільки по Києву.
             🔹 Доставка кур'єром <b>%s грн.</b> Після оформлення замовлення ми вам передзвонимо для уточння часу і місця\s
             🔹 Самовивоз. Сікорського 1, з 10 до 20 за попередньою домовленістю\s
             🔹 Новою поштою не відправляємо, бо макарнчики надто тендітні і не перживають таку доставку.\s
            """;

    public final static String PRICE_MESSAGE = """
            Маємо в асортименті три типи боксів:\s
             🔹 Бокс <b>S</b> містить <b>8</b> макаронів, <b>%s</b> грн;\s
             🔹 Бокс <b>M</b> містить <b>12</b> макаронів, <b>%s</b> грн;\s
             🔹 Бокс <b>L</b> містить <b>18</b> макаронів, <b>%s</b> грн;""";

    public final static String OVERALL_PRICE_START = "Загальна вартість замовлення: ";
    public final static String UAH = "грн";

    public final static String OVERALL_PRICE = BOLD_START + OVERALL_PRICE_START + BOLD_END + VALUE + ONE_SPACE + UAH + POINT;
}
