package leviathan143.loottweaker.common;

public interface ErrorHandler
{
    public void error(String format, Object... args);
    public void error(String message);
    public void warn(String format, Object... args);
    public void warn(String message);
}
