package org.inek.idservice;

/**
 *
 * @author muellermi
 */
public class ServiceStarter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> new IdService().run());
        thread.start();
        thread.join();
    }

}
