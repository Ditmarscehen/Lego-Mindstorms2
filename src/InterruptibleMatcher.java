import java.util.concurrent.*;
import java.util.regex.Pattern;

public class InterruptibleMatcher {

    public boolean matches(String text, String regex, boolean defaultValue, long timeOut, TimeUnit timeUnit) {
        FutureTask<Boolean> futureTask = new FutureTask<>(() ->
                Pattern.compile(regex).matcher(new InterruptibleCharSequence(text)).matches());
        new Thread(futureTask).start();
        try {
            return futureTask.get(timeOut, timeUnit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            futureTask.cancel(true);
            return defaultValue;
        }
    }

    public boolean matches(String text, String regex) {
        return matches(text, regex, false, 5000, TimeUnit.MILLISECONDS);
    }
}
