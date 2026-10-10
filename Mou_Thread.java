import java.util.concurrent.atomic.AtomicLong;

public class Mou_Thread extends Thread {

    static AtomicLong safeCounter = new AtomicLong(0);
    static long unsafeCounter = 0;

    long localCounter = 0;
    static boolean safeMode;
    int increments;

    Mou_Thread(int increments) {
        this.increments = increments;
    }

    public void run() {
        for (int i = 0; i < increments; i++) {
            if (safeMode) {
                safeCounter.incrementAndGet();
            } else {
                unsafeCounter++;
            }

            localCounter++;
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        int threads = Integer.parseInt(args[0]);
        int increments = Integer.parseInt(args[1]);
        safeMode = Boolean.parseBoolean(args[2]);

        safeCounter.set(0);
        unsafeCounter = 0;

        Mou_Thread[] workers = new Mou_Thread[threads];

        for (int i = 0; i < threads; i++) {
            workers[i] = new Mou_Thread(increments);
            workers[i].start();
        }

        long nonStaticTotal = 0;

        for (int i = 0; i < threads; i++) {
            workers[i].join();
            nonStaticTotal += workers[i].localCounter;
        }

        long staticTotal = safeMode
                ? safeCounter.get()
                : unsafeCounter;

        long expected = (long) threads * increments;
        long difference = Math.abs(staticTotal - nonStaticTotal);

        double percentage = nonStaticTotal == 0
                ? (staticTotal == 0 ? 0 : Double.NaN)
                : difference * 100.0 / nonStaticTotal;

        System.out.println("Threads: " + threads);
        System.out.println("Increments per thread: " + increments);
        System.out.println("Mode: " +
                (safeMode ? "Thread-Safe" : "Unsafe"));
        System.out.println("Expected Count: " + expected);
        System.out.println("Static Count: " + staticTotal);
        System.out.println("Non-Static Total: " + nonStaticTotal);
        System.out.println("Absolute Difference: " + difference);

        if (Double.isNaN(percentage)) {
            System.out.println("Percentage Difference: Undefined");
        } else {
            System.out.printf("Percentage Difference: %.2f%%%n",
                    percentage);
        }
    }
}
