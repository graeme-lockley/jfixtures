package za.co.no9.jfixture;

public class FixtureException extends Exception {
    public FixtureException(String message) {
        super(message);
    }

    public FixtureException(Exception ex) {
        super(ex);
    }
}
