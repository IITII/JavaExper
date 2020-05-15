import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author IITII
 */
public class Client {
    public static void main(String[] args) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(getFormatDate());
            }
        }, 0, 1000);
    }

    public static Calendar serverDate(DataInputStream serverInput) throws IOException {
        Calendar current = Calendar.getInstance();
        current.set(Calendar.YEAR, serverInput.readInt());
        current.set(Calendar.MONTH, serverInput.readByte());
        current.set(Calendar.DAY_OF_MONTH, serverInput.readByte());
        current.set(Calendar.HOUR_OF_DAY, serverInput.readByte());
        current.set(Calendar.MINUTE, serverInput.readByte());
        current.set(Calendar.SECOND, serverInput.readByte());
        return current;
    }

    public static String getFormatDate() {
        try {
            Socket socket = new Socket((String) null, 2007);
            socket.setKeepAlive(true);
            DataInputStream serverInput = new DataInputStream(socket.getInputStream());
            Calendar current = serverDate(serverInput);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            socket.close();
            return dateFormat.format(current.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
