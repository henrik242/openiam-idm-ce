package org.openiam.exception;

public class ScriptEngineException extends Exception {
    public ScriptEngineException() {
    }

    public ScriptEngineException(String message) {
        super(message);
    }

    public ScriptEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptEngineException(Throwable cause) {
        super(cause);
    }
}
