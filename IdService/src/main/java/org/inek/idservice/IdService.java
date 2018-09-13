package org.inek.idservice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import static org.inek.dataportal.api.helper.Const.REQUEST_ID;
import static org.inek.dataportal.api.helper.Const.REQUEST_TOKEN;
import static org.inek.dataportal.api.helper.Const.SERVICE_PORT;

@Singleton
@Startup
public class IdService {

    public static final Logger LOGGER = Logger.getLogger(IdService.class.getName());
    private final IdManager _idManager = new IdManager();
    @Resource private TimerService timerService;

    @PostConstruct
    private void setTimer() {
        LOGGER.log(Level.INFO, "Trigger IdService");
        timerService.createTimer(1000, "Created trigger for IdService");
    }

    // if we annotate the run metho with post construct, a deploy cannot be performed properly
    // because this method never ends
    // thus we start a timer during postconstruct which triggers the run method after delay
    @Timeout
    public void run() {
        LOGGER.log(Level.INFO, "Starting IdService");
        try (DatagramSocket socket = new DatagramSocket(SERVICE_PORT)) {
            while (true) {
                handleRequest(socket);
                _idManager.sweepOld();
            }
        } catch (SocketException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void handleRequest(final DatagramSocket socket) {
        try {
            DatagramPacket packet = receivePacket(socket);
            String inData = new String(packet.getData(), 0, packet.getLength());
            LOGGER.log(Level.FINE, "Data received: " + inData);
            String outData = determineAnswer(inData);
            sendAnswer(outData, packet, socket);
        } catch (SocketTimeoutException ex) {
            //ignore
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private DatagramPacket receivePacket(final DatagramSocket socket) throws SocketException, IOException {
        socket.setSoTimeout(100);
        byte[] inBuf = new byte[256];
        DatagramPacket packet = new DatagramPacket(inBuf, inBuf.length);
        socket.receive(packet);
        return packet;
    }

    private String determineAnswer(String inData) {
        String outData = "";
        if (inData.startsWith(REQUEST_TOKEN)) {
            String id = inData.substring(REQUEST_TOKEN.length());
            outData = _idManager.getToken(id);
        }
        if (inData.startsWith(REQUEST_ID)) {
            String token = inData.substring(REQUEST_ID.length());
            outData = _idManager.getId(token);
        }
        return outData;
    }

    private void sendAnswer(String outData, DatagramPacket packet, final DatagramSocket socket) throws IOException {
        if (outData.isEmpty()) {
            // do not send the missing answer - this allows the requester to receive the answer from another service
            return;
        }
        byte[] outBuf = outData.getBytes();
        DatagramPacket outpacket = new DatagramPacket(outBuf, outBuf.length, packet.getAddress(), packet.getPort());
        socket.send(outpacket);
        LOGGER.log(Level.FINE, "Data send: " + outData);
    }

}
