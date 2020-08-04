package io.github.purpleloop.tools.svg.tools;

/** A simple XML Exception. */
public class XmlException extends Exception {

    /** Serial tag. */
    private static final long serialVersionUID = 1415135916359137272L;

    /**
     * Creates an XML Exception
     * 
     * @param message Message of the exception
     * @param cause cause of exception
     */
    public XmlException(String message, Throwable cause) {
        super(message, cause);
    }

}
