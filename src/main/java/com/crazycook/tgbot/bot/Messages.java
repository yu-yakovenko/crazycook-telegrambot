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

    public final static String START_USER = "Привіт \uD83D\uDC4B, раді тебе вітати в нашому чат-боті для замовлення макарон. Ось головне меню, обери, що тебе цікавить: ";
    public final static String START_ADMIN = "Привіт, мій любий адмін ❤️ Що будемо робити сьогодні?";
    public final static String CUSTOMER_MENU = "Меню користувача: ";

    //cart
    public static final String IN_YOUR_CART = "В твоєму кошику зараз: \n";
    public static final String YOUR_CART_IS_EMPTY = "\uD83D\uDE32 Тут пуст! Жодного макарончика \uD83E\uDD37\u200D♂️";
    public final static String CART_WAS_REFRESH = "Кошик очищено, зараз в ньому нічого немає.";
    public static final String DELIVERY_NEEDED = "Для того щоб підтвердити замовлення, необхідно вказати спосіб доставки і адресу(якщо доставка кур'єром).";
    public static final String CART_CONFIRM_REQUIREMENT = "Для того, щоб розмістити замовлення необхідно і достатньо, щоб замовлення містило хочаби один бокс, всі бокси були повністю заповнені смаками, а також вибрано спосіб доставки. Додатково ти можеш додати коментар до замовлення та застосувати промокод.";
    public static final String CART_COMPLETE = LINE_END + LINE_END + "Кошик сформовано, всі бокси, що зараз є у кошику заповнено смаками. " + DELIVERY_NEEDED;
    public static final String THANKS_MESSAGE = "Дякуємо за замовлення, наш менеджер скоро звяжеться з вами.";

    //order
    public static final String ORDER_EMPTY = "В твоєму замовленні ще нічого немає.";
    public static final String IN_YOUR_ORDER = "В твоєму замовленні: ";
    public static final String PLACE_ORDER = "щойно оформив замовлення.";
    public static final String ORDER_NUMBER = "Замовлення номер: ";
    public static final String ORDER_MARKED_AS_DONE = "Замовлення номер %s відмічено як виконане.";
    public static final String ORDER_NOT_FIND = "Замовлення номер %s не знайдене.";

    //box
    public static final String BOX_COMPLETE_MIX = "Бокс заповнено міксом смаків. ";
    public static final String BOXES_COMPLETE = "Бокси заповнено міксом смаків. ";
    public static final String BOX_COMPLETE = "%d-й %s бокс заповнено.";
    public static final String BOX_ADDED = "Супер, ми додали %s бокс до твого кошика.  Клікай кнопки щоб додати чи прибрати ще бокси, або переходь до наповнення боксу смаками.";
    public static final String ONE_MORE_BOX_ADDED = "Супер, ми додали ще один %s бокс до твого кошика. Клікай кнопки щоб додати чи прибрати ще бокси, або переходь до наповнення боксів смаками.";
    public static final String ONE_MORE_BOX_REMOVED = "Ок, ми прибрали один %s бокс з твого кошика. Клікай кнопки щоб додати чи прибрати ще бокси, або переходь до наповнення боксів смаками.";
    public final static String CHOOSE_BOX = "Обери розмір боксу. \n\nЗліва кнопки з розмірами боксів, клік по ним додає один бокс. Навпроти кожного розміру, справа є кнопка -1, яка прибере з твого кошика один такий бокс. \n";
    public final static String EMPTY_BOX = RED_DIAMOND + ONE_SPACE + BOLD_START + VALUE + " пустих " + VALUE + " боксів" + BOLD_END + LINE_END;
    public final static String START_BOX_MESSAGE = "Починаємо збирати %s бокс номер %d. Зліва кнопки з назвою смаків, клік по ним додає до бокса один такий макарон. Справа нвпроти кожного смаку є кнопка ʼ-1ʼ яка прибирає один макарон. \n\nТакож ти можеш нажати кнопку 'зберіть мені мікс' і ми зберемо тобі бокс з різними смаками. \n\nДодай смак до боксу:";
    public final static String IN_PROGRESS_BOX_MESSAGE = "Продовжуємо збирати %s бокс номер %d. В боксі зараз: ";

    //flavor
    public final static String FLAVOR_MESSAGE = BOLD_START + "Зараз в наявності є такі смаки: " + BOLD_END + LINE_END;
    public static final String NEW_FLAVOR_ADDED = "Новий смак '%s' створено, наявність автоматично встановлено 'в наявності'.";
    public static final String INPUT_NEW_FLAVOR = "Введи назву нового смаку";
    public static final String IN_STOCK_FLAVORS = "Це смаки які зараз <b>є в наявності</b>. Натисни кнопку, щоб поміняти статус цього смаку на 'немає в наявності'";
    public static final String NOT_IN_STOCK_FLAVORS = "Це смаки яких зараз <b>немає в наявності</b>. Натисни кнопку, щоб поміняти статус цього смаку на 'є в наявності'";
    public static final String IN_STOCK_WAS_CHANGED = "Статус смаку '%s' змінено на <b>%s</b>";

    public static String moreFlavorsPossible(int number) {
        return "Ще можна додати " + macaronEnding(number) + ". Обери наступний смак:";
    }

    //contact
    public static final String REQUEST_CONTACT = "Будь ласка натисніть кнопку 'Поділитися контактом', щоб ми могли звязатися з вами для уточнення деталей доставки";
    public static final String PHONE_NUMBER_IS = "Телефон: ";
    public static final String CUSTOMER_CONTACT = VALUE + ONE_SPACE + VALUE + ONE_SPACE + AT_SIGN + VALUE + ONE_SPACE +
            BOLD_START + PHONE_NUMBER_IS + VALUE + BOLD_END + LINE_END + LINE_END;
    public static final String CUSTOMER_JUST_PLACED_ORDER = VALUE + ONE_SPACE + VALUE + ONE_SPACE + AT_SIGN + VALUE
            + ONE_SPACE + PLACE_ORDER + BOLD_START + PHONE_NUMBER_IS + VALUE + BOLD_END + LINE_END + LINE_END;

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
    public final static String CHANGE_PRICE_MENU = "Вибери яку ціну ти хочеш змінити.";
    public final static String BOX_PRICE = "Бокс %s - %s грн.";
    public final static String DELIVERY_PRICE = "Доставка - %s грн.";
    public final static String INPUT_NEW_PRICE = "Введи нову ціну";
    public final static String PRICE_WAS_CHAGED = "Ціну змінено.";
    public final static String INVALID_PRICE_VALUE = "Невірне значення ціни. Я розумію цілі та дробні значення. Якщо ти вводиш дробне значення, використовуй крапку як роздыловий знак '0.0' (не кому)";

    //promo
    public final static String PROMO_ADDED = "Додали промокод " + VALUE + " , що дає знижку " + VALUE + "%%.";
    public final static String PROMO_EXPIRED = "Вибач, цей промокод вже просрочено, він діяв до " + VALUE;
    public final static String WRONG_PROMO = "Такого промокоду в нас немає.";
    public final static String INPUT_PROMO = "Введи промокод";
    public static final String INPUT_NEW_PROMO = "Введи новий промокод у форматі: " + LINE_END + "назва промокоду; 99; 2023-12-31" + LINE_END + " де 99 це процент знижки який надає промокод, а 2023-12-31 дата завершення. Головне не забудь поставити символ ';' між назвою, процентом і датою";
    public static final String PERCENT_WRONG = "Формат вводу проценту невірний, щоб я зміг зрозуміти, пиши процент просто числом без додаткових символів. І звісно підходять значення тільки від 1 до 100";
    public static final String DATE_WRONG = "Формат вводу дати невірний. Я дозумію формат yyyy-mm-dd, тобто нарпиклад 1991-08-24  це валідний ввод дати";
    public static final String NEW_PROMO_ADDED = "Новий промокод '%s', що надає знижку у розмірі %s відсотків, та діє до %s, додано.";
    public static final String PROMO_ALREADY_EXISTS = "Промокод з такою назвою вже існує.";

    public static String boxEnding(int number) {
        return switch (number) {
            case 1 -> "1 бокс";
            case 2, 3, 4 -> number + " бокси";
            default -> number + " боксів";
        };
    }

    public static String macaronEnding(int number) {
        return switch (number) {
            case 1 -> "1 макарон";
            case 2, 3, 4 -> number + " макарони";
            default -> number + " макаронів";
        };
    }

    public static String flavorMixStr(int number, String size) {
        String contain = "містять";
        if (number == 1) {
            contain = "містить";
        }
        return BLUE_DIAMOND + BOLD_START + boxEnding(number) + " розміру " + size + ", що " + contain + " мікс смаків" + BOLD_END + LINE_END;
    }

}
