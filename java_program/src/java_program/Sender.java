package java_program;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


public class Sender {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final long timeInterval = 100;
		
		Random random = new Random();
		
		/* ������ ���� */
		Runnable runnableForInsert = new Runnable() {
		public void run() {
			String useFor = "insert_master"; // ��� Ÿ��
			
			while(true) {
				// DB ����
				DBConnection dc = new DBConnection();
				
				// ���� �ð��� ���Ѵ�.
				LocalDateTime localDate = LocalDateTime.now();
				Timestamp time = Timestamp.valueOf(localDate);
				
				RandomNumber randomNumber = new RandomNumber();
				randomNumber.setRandom_number(random.nextInt());
				randomNumber.setRegistered_datetime(time);
				
				// ����
				dc.ConnectDB(randomNumber, useFor);
				
				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		  }	
		};
		
		/* ������ ���� */
		Runnable runnableForSender = new Runnable() {
			public void run() {
				String useFor = "send_master"; // ��� Ÿ��
				
				while(true) {
					// DB ����
					DBConnection dc = new DBConnection();
					
					// �ڵ� close
					try(ServerSocket server = new ServerSocket()) {
						// ���� �ʱ�ȭ
						InetSocketAddress isa = new InetSocketAddress(9999);
						server.bind(isa);
						
						System.out.println("Server Initialize Complete");

						// Listen ���
						Socket client = server.accept();
						client.setSoTimeout(2000); // Ÿ�� �ƿ� ����
						System.out.println("Connection");
						
						// send, receiver ��Ʈ�� �޾ƿ���
						// �ڵ� close
						try(ObjectOutputStream sender = new ObjectOutputStream(client.getOutputStream());
								ObjectInputStream receiver = new ObjectInputStream(client.getInputStream());) {
							
							// Ŭ���̾�Ʈ�� ���� ID �� �޾ƿ���
							int random_number_id = ((RandomNumber) receiver.readObject()).getRandom_number(); // ����
							
							RandomNumber randomNumber = new RandomNumber();
							randomNumber.setRandom_number_id(random_number_id);
							
							// �Ѱ� ���� random_number_id�� ���ؼ� master�� �ִ� �����͸� �����´�.
							List<RandomNumber> randomNumberList = (List<java_program.RandomNumber>) dc.ConnectDB(randomNumber, useFor).get("resultList");
									//randomNumberDao.selectRandomNumberList(randomNumber);
							
							System.out.println("������ ������...");
							
							// ��ü ���۽� ����ȭ �ؼ� ����
							RandomNumber temp = new RandomNumber();
							temp.setRandom_number_list(randomNumberList);
							
							sender.writeObject(temp); // ����
							
							sender.flush();
						}
								
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				
			}
		};
		
		
		Thread threadForInsert = new Thread(runnableForInsert);
		threadForInsert.start();
		
		Thread threadForSend = new Thread(runnableForSender);
		threadForSend.start();
	}

}
