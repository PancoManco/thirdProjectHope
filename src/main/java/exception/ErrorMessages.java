package exception;

public class ErrorMessages {
    private ErrorMessages() {
    }
        public static class DataBaseError {

            public static final String ERROR_LOADING_PROPERTIES= "Ошибка при загрузке конфигурации из properties";
            public static final String ERROR_CONNECTION_POOL= "Ошибка при получении соединения из пула";
            public static final String CONFIGURATION_LOAD_ERROR = "Ошибка загрузки настроек из файла db.properties.";
        }

        public static class CurrencyError {
            public static final String ERROR_SAVING_CURRENCY_TEMPLATE =
                    "Ошибка при сохранении валюты в базу данных: код валюты='%s', имя валюты='%s', детальнее: %s";
            public static final String ERROR_GETTING_CURRENCIES_LIST_TEMPLATE =
                    "Ошибка получения списка валют из базы данных: запрос SQL=%s, детальнее: %s";
            public static final String ERROR_FINDING_CURRENCY_BY_CODE_TEMPLATE =
                    "Ошибка при поиске валюты по коду из базы данных : %s. Детали: %s";
        }   public static final String CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE =
                    "Валюта с кодом '%s' не найдена";

    public static class ParameterError {
        public static final String SIGN_INVALID = "Знак валюты должен содержать от 1 до 3 символов, включая только буквы и цифры без пробелов и пунктуации.";
        public static final String CURRENCY_CODE_INVALID = "Код должен содержать ровно 3 заглавные английские буквы!";
        public static final String CURRENCY_NAME_INVALID = "Имя должно содержать только английские буквы и иметь длину от 1 до 30 символов.";
        public static final String PARAMETER_CANNOT_BE_NULL_OR_EMPTY = "Поля ввода или запроса не могут пустыми или null";
        public static final String CURRENCY_CODE_MISSING = "Код валюты отсутсвует в адресе!";
        public static final String ERROR_UNIQUE_CONSTRAINT_VIOLATION_CURRENCY = "Валюта c кодом %s уже существует !";
        public static final String SERIALIZATION_ERROR_MESSAGE = "Ошибка при сериализации объекта в JSON.";
        public static final String PARAMETER_CURRENCY_CANNOT_BE_NULL_OR_EMPTY = "Код валюты отсутствует в адресе";
        public static final String REQUIRED_FORM_FIELD_MISSING = "Отсутствует обязательное поле формы";
        public static final String STRING_TO_PARSE_IS_INVALID = "Не удалось конвертировать введенную строку в число";
        public static final String CURRENCIES_CODES_EQUAL = "Валюты должны быть разными, а не одинаковыми ";
        public static final String NUMBER_NOT_IN_RANGE_TEMPLATE = "Вводимое число должно быть между %s и %s";
        private ParameterError() {
        }

        public static class ExchangeRatesError {
            public static final String ERROR_UNIQUE_CONSTRAINT_VIOLATION_EXRATE_TEMPLATE="Конвертация %s уже существует!";
            public static final String UNABLE_TO_CONVERT  = "Валюта не найдена ";

            public static final String FAILED_TO_RETRIEVE_EXCHANGE_RATES = "Ошибка при получение списка курса обмена валют.Проблемы с доступом к БД!";
            public static final String FAILED_TO_CREATE_EXCHANGE_RATE = "Ошибка при создании курса обмена валют. Проблемы с доступом к БД! ";
            public static final String FAILED_TO_UPDATE_EXCHANGE_RATE = "Ошибка при обновление курса обмена валют.Проблемы с доступом к БД!";
            public static final String FAILED_TO_RETRIEVE_PAIR_BY_CODE = "Ошибка при получение пары обмена курса валют. Проблемы с доступом к БД!";
        }
    }
}
