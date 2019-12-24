import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	ServerSocket serverSocket = null;
	Socket socket = null;

	public static void main(String[] args) {

		TCPServer tcpServer = new TCPServer();
		tcpServer.runServer();

	}

	public void runServer() {

		try {
			serverSocket = new ServerSocket(9000);
			System.out.println(serverSocket.getInetAddress().toString());

			while (true) {

				socket = serverSocket.accept(); // 기다리고 있다가, 클라이언트소켓이 접근하면, 업셋트 메소드를 실행해서 서버소켓을 리턴함.
				System.out.println(socket.toString());

				Thread serverSocketThread = new Thread(new ServerSocketThread(socket));
				serverSocketThread.start();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class ServerSocketThread implements Runnable {

		Socket socket;
		BufferedReader in = null;
		BufferedReader in2 = null;
		PrintWriter out = null;

		public ServerSocketThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 인풋을 받는 버퍼리더 만듬.
				in2 = new BufferedReader(new InputStreamReader(System.in));
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))); // 아웃풋을 내주는
																												// 라이터를
																												// 만듬.

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
						System.out.print("클라이언트로 보낼 메세지 : ");
						String data = in2.readLine();
						out.println(data);
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
						String str2 = in.readLine(); // 클라이언트로 부터 들어 오는메시지 읽기
						if (str2 == null) {
							socket.close();
							return;
						} else {
							System.out.println("클라이언트로 부터들어온 메세지 : " + str2);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

}