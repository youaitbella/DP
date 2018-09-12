package org.inek.idservice;

import org.inek.idservice.IdService;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.inek.idservice.IdManager.VALID_TIME;
import static org.inek.dataportal.api.helper.Const.REQUEST_ID;
import static org.inek.dataportal.api.helper.Const.REQUEST_TOKEN;
import static org.inek.dataportal.api.helper.Const.SERVICE_PORT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class BroadcastTest {

    private static final String ID = "12345";
    private static final int DELAY = 10;

    @BeforeAll
    private static void startService() throws InterruptedException {
        System.out.println("Start service");
        Thread thread = new Thread(() -> new IdService().run());
        thread.start();
        Thread.sleep(100); // let thread start properly
        System.out.println("Service started");
    }

    @Test
    public void storeIdAnRetrieveItByToken() throws IOException {
        String token = request(REQUEST_TOKEN, ID);
        String id = request(REQUEST_ID, token);
        assertThat(id).isEqualTo(ID);
    }

    @Test
    public void storeIdAndReceiveNothingBackAfterDelay() throws IOException, InterruptedException {
        String token = request(REQUEST_TOKEN, ID);
        Thread.sleep(VALID_TIME + DELAY);

        Throwable exception = Assertions.assertThrows(SocketTimeoutException.class, () -> {
            request(REQUEST_ID, token);
        });
        assertThat(exception.getClass()).isEqualTo(SocketTimeoutException.class);

    }

    private String request(String type, String data) throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(100);
            broadcast(socket, type + data);
            return receive(socket);
        }
    }

    private static void broadcast(DatagramSocket socket, String broadcastMessage) throws IOException {
        InetAddress address = InetAddress.getByName("192.168.0.255");
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, SERVICE_PORT);
        socket.send(packet);
    }

    private static String receive(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String data = new String(packet.getData(), 0, packet.getLength());
        return data;
    }

}
