package autoclicker;
import java.awt.Robot;
import java.awt.event.InputEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.util.InputMismatchException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.Scanner;

public class Main extends Thread implements NativeKeyListener {

    public static boolean isRunning = false;
    public static int delay;

    public void nativeKeyTyped(NativeKeyEvent event) {}
    public void nativeKeyReleased(NativeKeyEvent event) {}

    public void nativeKeyPressed(NativeKeyEvent event) {
        if(!isRunning && event.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
            Calendar cal = Calendar.getInstance();
            
            isRunning = true;
            System.out.println("\nAutoclicker activated at " + cal.getTime());
        }
        if(isRunning && event.getKeyCode() == NativeKeyEvent.VC_LEFT) {
            Calendar cal = Calendar.getInstance();

            isRunning = false;
            System.out.println("Autoclicker ended at " + cal.getTime());
        }
    }

    public void run() {
        try {
            Robot robot = new Robot();

            while(true) {
                if(isRunning) {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                Thread.sleep(delay);
            }
        } catch(Exception e) {}
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\nHow much delay between clicks? (milliseconds)");
        while(sc.hasNext()) {
            boolean hasInputMismatch = false;

            try {
                delay = sc.nextInt();
            } catch(InputMismatchException e) {
                hasInputMismatch = true;
                sc.next();
            }
            if(delay > 0 && !hasInputMismatch) {
                break;
            }
            System.out.println("\nInvalid input. Please try again");
            continue;
        }
        System.out.println("\nPress right arrow key to activate"
        + "\nPress left arrow key to end"
        + "\nHave fun!");
        sc.close();

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try { GlobalScreen.registerNativeHook(); } catch(Exception e) {}
        GlobalScreen.addNativeKeyListener(new Main());

        Main thread2 = new Main();
        thread2.start();
    }
}
