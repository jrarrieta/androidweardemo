package androidsummit.androidweardemo.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {

    private static Handler uiHandler;

    /**
     * Check to see whether current thread is also the UI Thread
     *
     * @return TRUE when current thread is the UI Thread
     */
    public static boolean isOnUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * @deprecated  use isOnUiThread
     */
    @Deprecated
    public static boolean isOnMainThread() { return isOnUiThread(); }

    /**
     * Determine if we are currently on the UI Thread, if we are, execute the ACTION requested, otherwise action is posted to the UI Thread
     * and function returns without delay
     *
     * @param action procedure to run on the UI Thread
     */
    public static void runOnUiThread(final Runnable action) {
        if (isOnUiThread()) {
            action.run();
        } else {
            postOnUiThread(action);
        }
    }

    /**
     * same as calling #runOffUiThread(action,false)
     */
    public static void runOffUiThread(final Runnable action) {
        runOffUiThread(action, false);
    }


    /**
     * Determine if we are on the UI Thread or not.  If we are NOT on the UI Thread, we will execute the action in the current thread.  If
     * we are on the UI Thread, we will spawn a new thread to execute ACTION
     *
     * @param action procedure to run off the UI Thread
     * @param wait   true if we should wait for action to complete before returning to calling process
     */
    public static void runOffUiThread(final Runnable action, final boolean wait) {
        if (isOnUiThread()) {
            runOnNewThread(action, wait);
        } else {
            action.run();
        }
    }

    /**
     * Same as calling #runOnNewThread( action, false )
     */
    public static void runOnNewThread(final Runnable action) {
        runOnNewThread(action, false);
    }

    /**
     * Always spawn a new, non-UI thread process to execute ACTION
     *
     * @param action procedure to run on the new Thread
     * @param wait   true if we should wait for action to complete before returning to calling process
     */
    public static void runOnNewThread(final Runnable action, final boolean wait) {
        final Runnable threadRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    action.run();
                    if (wait) {
                        this.notify();
                    }
                }
            }
        };

        synchronized (threadRunnable) {
            new Thread(threadRunnable).start();
            waitForAction(threadRunnable, wait);
        }
    }

    /**
     * Same as calling #postOnUiThread(action,false)
     */
    public static void postOnUiThread(final Runnable action) {
        postOnUiThread(action, false);
    }

    /**
     * Always post action to the UI Thread for execution
     *
     * @param action procedure to run on the UI Thread
     * @param wait   true if we should wait for action to complete before returning to calling process
     */
    public static void postOnUiThread(final Runnable action, final boolean wait) {
        if( uiHandler == null ) {
            uiHandler = new Handler(Looper.getMainLooper());
        }

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (action) {
                    action.run();
                    if (wait) {
                        action.notify();
                    }
                }
            }
        });

        waitForAction(action, wait);

    }

    protected static void waitForAction(final Runnable action, boolean wait) {
        if (wait) {
            synchronized (action) {
                try {
                    action.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
