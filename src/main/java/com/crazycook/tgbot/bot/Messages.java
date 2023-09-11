package com.crazycook.tgbot.bot;

public class Messages {
    public static final String BOLD_START = "<b>";
    public static final String BOLD_END = "</b>";
    public static final String LINE_END = "\n";
    public static final String VALUE = "%s";
    public static final String POINT = ".";
    public static final String ONE_SPACE = " ";
    public static final String FOUR_SPACES = "    ";
    public static final String AT_SIGN = "@";

    public static final String RED_DIAMOND = "♦️";
    public static final String BLUE_DIAMOND = "\uD83D\uDD39";
    public static final String YELLOW_DIAMOND = "\uD83D\uDD38";

    public final static String START_MESSAGE = "Привіт \uD83D\uDC4B, раді тебе вітати в нашому чат-боті для замовлення макарон. Ось головне меню, обери, що тебе цікавить: ";

    //cart
    public static final String IN_YOUR_CART = "В твоїй корзині зараз: \n";
    public static final String YOUR_CART_IS_EMPTY = "\uD83D\uDE32 Тут пуст! Жодного макарончика \uD83E\uDD37\u200D♂️";
    public final static String CART_WAS_REFRESH = "Корзину очищено, зараз в ній нічого немає.";
    public static final String CART_COMPLETE = "Корзина сформована повністю. ";
    public static final String THANKS_MESSAGE = "Дякуємо за замовлення, наш менеджер скоро звяжеться з вами.";

    //order
    public static final String ORDER_EMPTY = "В твоєму замовленні ще нічого немає.";
    public static final String IN_YOUR_ORDER = "В твоєму замовленні: ";
    public static final String PLACE_ORDER = "щойно оформив замовлення.";

    //box
    public static final String BOX_COMPLETE_MIX = "Бокс заповнено міксом смаків. ";
    public static final String BOXES_COMPLETE = "Бокси заповнено міксом смаків. ";
    public static final String BOX_COMPLETE = "Бокс заповнено.";
    public static final String MESSAGE_FORMAT = "Супер, ми додали %s %s боксів до твого кошика.";
    public final static String CHOOSE_BOX = "Обери розмір боксу: \n";
    public final static String SIZE_NUMBER = "Напиши цифрою скільки %s боксів ти хочеш додати до замовлення? \n";
    public final static String EMPTY_BOX = RED_DIAMOND + ONE_SPACE + BOLD_START + VALUE + " пустих " + VALUE + " боксів" + BOLD_END + LINE_END;
    public final static String START_BOX_MESSAGE = "Починаємо збирати %s бокс номер %d. Додай смак до боксу:";
    public final static String IN_PROGRESS_BOX_MESSAGE = "Продовжуємо збирати %s бокс номер %d. Зараз в ньому вже є %d макарон. Можна додати ще %d. Додай смак до боксу:";

    //flavor
    public final static String FLAVOR_MESSAGE = BOLD_START + "Зараз в наявності є такі смаки: " + BOLD_END + LINE_END;
    public final static String FLAVOR_NUMBER_MESSAGE = "Напиши цифрою скільки макаронів зі смаком ʼ%sʼ ти хочеш додати до цього боксу?\n";
    public final static String BOX_OVERFLOW = "Упс, схоже що в цей бокс ще %s макарончиків не влізе.\n";
    public static final String FLAVOR_ADDED = "Ми додали %d макарон зі смаком %s в %d-й %s бокс. ";
    public static final String MORE_FLAVORS_POSSIBLE = "Ще можна додати %d макарон. Обери наступний смак:";

    //contact
    public static final String REQUEST_CONTACT = "Будь ласка натисніть кнопку 'Поділіться контактом', щоб ми могли звязатися з вами для уточнення деталей доставки";
    public static final String PHONE_NUMBER_IS = "Телефон: ";

    //address
    public final static String ADDRESS_SAVED = "Адресу збережено. ";
    public final static String ADDRESS = BOLD_START + "Адреса доставки: " + BOLD_END;
    public static final String INPUT_ADDRESS = "Введи адресу доставки";

    //incorrect number REACTION
    public static final String NEGATIVE_NUMBER_REACTION = "\uD83E\uDD28 Серйозно, від'ємне значення? Ти, що тестувальник(ця) \uD83D\uDE01? \nЯкщо в тебе є бажання допомогти нам тестувати цей бот, звернись до наших адмінів.";
    public static final String FLOATING_NUMBER_REACTION = "\uD83E\uDDD0 Це що дробне значенн? Ми продаємо лише цілі бокси і цілі макарони.";
    public static final String TO_LONG_INTEGER_NUMBER_REACTION = "\uD83D\uDE02 Ого, оце ти ласун(ка)! Але в нас стільки макаронів немає, спробуй написати меншу кількість.";

    //comment
    public static final String COMMENT = "Коментар: ";
    public static final String LEAVE_COMMENT = "Якщо хочешь, можешь залишити коментар до замовлення. Наприклад, можешь вказати як краще з тобою звязуватись - писати в телеграмі, чи дзвонити на телефон. Можешь написати побажання до дати і часу доставки, або будь яке інше побажання.";
    public static final String WAITING_COMMENT = "\uD83E\uDDDE\u200D♂️ Я слухаю, пиши.";
    public static final String COMMENT_ADDED = "Дякую, коментар додали.";
    public static final String COMMENT_IS = "Коментар: ";


    //delivery
    public final static String DELIVERY_MESSAGE = """
            Доставка можлива тільки по Києву.
             🔹 Доставка кур'єром <b>%s грн.</b> Після оформлення замовлення ми вам передзвонимо для уточння часу і місця\s
             🔹 Самовивоз. Сікорського 1, з 10 до 20 за попередньою домовленістю\s
             🔹 Новою поштою не відправляємо, бо макарнчики надто тендітні і не перживають таку доставку.\s
            """;
    public static final String DELIVERY_METHOD = "Cпосіб доставки: ";

    //price
    public final static String PRICE_MESSAGE = """
            Маємо в асортименті три типи боксів:\s
             🔹 Бокс <b>S</b> містить <b>8</b> макаронів, <b>%s</b> грн;\s
             🔹 Бокс <b>M</b> містить <b>12</b> макаронів, <b>%s</b> грн;\s
             🔹 Бокс <b>L</b> містить <b>18</b> макаронів, <b>%s</b> грн;""";
    public final static String OVERALL_PRICE_START = "Загальна вартість замовлення: ";
    public final static String UAH = "грн";
    public final static String OVERALL_PRICE = BOLD_START + OVERALL_PRICE_START + BOLD_END + VALUE + ONE_SPACE + UAH + POINT;
    public final static String PRICE_WITH_PROMO = BOLD_START + "Промокод: " + BOLD_END + VALUE
            + LINE_END + BOLD_START + "Вартість із промокодом: " + BOLD_END + VALUE;

    //promo
    public final static String PROMO_ADDED = "Додали промокод " + VALUE + " , що дає знижку " + VALUE + "%%.";
    public final static String PROMO_EXPIRED = "Вибач, цей промокод вже просрочено, він діяв до " + VALUE;
    public final static String WRONG_PROMO = "Такого промокоду в нас немає.";
    public final static String INPUT_PROMO = "Введи промокод";
}
