package java_program;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class Receiver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final long timeIntervalForReceiver = 1000;
		
		Runnable runnableForReceiver = new Runnable() {
			public void run() {
				String useFor = "receiver_backup"; // ��� Ÿ��
			
				while(true) {
					// DB ����
					DBConnection dc = new DBConnection();
				
					try (Socket client = new Socket()) {
						// Ŭ���̾�Ʈ �ʱ�ȭ
						InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 9999);
						// ����
						client.connect(isa);
						System.out.println("Client Connection...");
						
						// send, receiver ��Ʈ�� �޾ƿ���
						try(ObjectOutputStream sender = new ObjectOutputStream(client.getOutputStream());
								ObjectInputStream receiver = new ObjectInputStream(client.getInputStream());) {
							// ������ ������ ����
							RandomNumber randomNumber = new RandomNumber();
							
							int randomNumberCount = (int) dc.ConnectDB(randomNumber, useFor + "_count").get("resultCount");
									//randomNumberDao.selectRandomNumberCount();
							int random_number_id = 0;
							
							if(randomNumberCount > 0) { // �����Ͱ� ������
								random_number_id = (int) dc.ConnectDB(randomNumber, useFor + "_select").get("resultMaxId");
										//randomNumberDao.selectMaxRandomNumber();
							}
							
							RandomNumber temp = new RandomNumber();
							temp.setRandom_number(random_number_id);
							
							sender.writeObject(temp); // ����
							sender.flush();
							
							List<RandomNumber> randomNumberList = ((RandomNumber) receiver.readObject()).getRandom_number_list(); // ����
							
							for(RandomNumber ran : randomNumberList) {
								
								dc.ConnectDB(ran, useFor + "_insert");
								//randomNumberDao.insertRandomNumberToBackup(ran);
							}
							
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
					
					try {
						Thread.sleep(timeIntervalForReceiver);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		};
		
		Thread threadForInsert = new Thread(runnableForReceiver);
		threadForInsert.start();
	}

}
