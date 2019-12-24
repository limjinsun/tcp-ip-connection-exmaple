import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient2 {

	Socket socket = null; // Server와 통신하기 위한 Socket
	BufferedReader in = null; // Server로부터 데이터를 읽어들이기 위한 입력스트림
	BufferedReader in2 = null; // 키보드로부터 읽어들이기 위한 입력스트림
	PrintWriter out = null; // 서버로 내보내기 위한 출력 스트림
	InetAddress ia = null;

	public static void main(String[] args) {

		TCPClient2 client = new TCPClient2();
		client.runClient();

	}

	public void runClient() {

		try {
			ia = InetAddress.getByName("0.0.0.0"); // 서버로 접속
			socket = new Socket(ia, 9000);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			in2 = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

			System.out.println("클라이언트 소켓 : " + socket.toString());

			Thread sender = new Thread(new SendingThread());
			sender.start();

			Thread reciever = new Thread(new ReceivingThread());
			reciever.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private class SendingThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					System.out.print("서버로 보낼 메세지 : ");
					String data = in2.readLine(); // 키보드로부터 입력
					out.println(data); // 서버로 데이터 전송
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private class ReceivingThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					String str2 = in.readLine(); // 서버로부터 되돌아오는 데이터 읽어들임
					System.out.println("서버로부터 되돌아온 메세지 : " + str2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
