package exception;

public class ErrorMessages {
    private ErrorMessages() {   }

    public static class ParameterError {
        public static final String SIGN_INVALID = "Знак валюты должен содержать от 1 до 3 символов, включая только буквы и цифры без пробелов и пунктуации.";
        public static final String CURRENCY_CODE_INVALID = "Код должен содержать ровно 3 заглавные английские буквы!";
        public static final String CURRENCY_NAME_INVALID = "Имя должно содержать только английские буквы и иметь длину от 1 до 30 символов.";
        public static final String PARAMETER_CANNOT_BE_NULL_OR_EMPTY= "Поля ввода или запроса не могут пустыми или null";


        private ParameterError() { }
    }
}
