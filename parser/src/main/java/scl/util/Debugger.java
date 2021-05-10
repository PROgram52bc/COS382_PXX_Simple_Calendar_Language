package scl.util;

public class Debugger {

    private Debugger() { }

    /** The maximum level of debug to print.
     * 
     * to disable debug, set this value to 0. 
     **/
    public final static int DEBUGLEVEL = 10;

    /** 
     * Log a message to the console.
     * @param msg the message to be logged.
     * @param level the level of debug, must be greater than 0 
     **/
    public static void log(int level, Object msg) {
        if (0 < level && level <= DEBUGLEVEL) {
            System.out.print("[" + level + "] ");
            System.out.println(msg);
        }
    }

    /**
     * Log a message to the console (with a level equal to 1)
     * @param msg the message to be logged.
     **/
    public static void log(Object msg) {
        log(1, msg);
    }
}
