package purpleloop.tools.svg.model;

/** Exception class for SVG objects. */
public class SvgException extends Exception {

    /** Serialization tag. */
    private static final long serialVersionUID = 822042829218043629L;

    /**
     * Creates an exception for a given message.
     * 
     * @param message error message
     */
    public SvgException(String message) {
        super(message);
    }

}
