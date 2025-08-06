package exception;

public class ErrorMessages {
    private ErrorMessages() {
    }
        public static class DataBaseError {
        public static final String DATABASE_ERROR = "Database Error";

        }

        public static class CurrencyError {
            public static final String ERROR_SAVING_CURRENCY_TEMPLATE =
                    "Ошибка при сохранении валюты в базу данных: код валюты='%s', имя валюты='%s', детальнее: %s";
            public static final String ERROR_GETTING_CURRENCIES_LIST_TEMPLATE =
                    "Ошибка получения списка валют из базы данных: запрос SQL=%s, детализация: %s";
            public static final String ERROR_FINDING_CURRENCY_BY_CODE_TEMPLATE =
                    "Ошибка при поиске валюты по коду: %s. Детали: %s";
        }   public static final String CURRENCY_NOT_FOUND_MESSAGE_TEMPLATE =
            "Валюта с кодом '%s' не найдена";

    public static class ParameterError {
        public static final String SIGN_INVALID = "Знак валюты должен содержать от 1 до 3 символов, включая только буквы и цифры без пробелов и пунктуации.";
        public static final String CURRENCY_CODE_INVALID = "Код должен содержать ровно 3 заглавные английские буквы!";
        public static final String CURRENCY_NAME_INVALID = "Имя должно содержать только английские буквы и иметь длину от 1 до 30 символов.";
        public static final String PARAMETER_CANNOT_BE_NULL_OR_EMPTY = "Поля ввода или запроса не могут пустыми или null";
        public static final String ERROR_DUPLICATE_VALUES = "Валюта c кодом %s уже существует !";

        private ParameterError() {
        }
    }
}
