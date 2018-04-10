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
		
		/* 데이터 적재 */
		Runnable runnableForInsert = new Runnable() {
		public void run() {
			String useFor = "insert_master"; // 사용 타입
			
			while(true) {
				// DB 연결
				DBConnection dc = new DBConnection();
				
				// 현재 시간을 구한다.
				LocalDateTime localDate = LocalDateTime.now();
				Timestamp time = Timestamp.valueOf(localDate);
				
				RandomNumber randomNumber = new RandomNumber();
				randomNumber.setRandom_number(random.nextInt());
				randomNumber.setRegistered_datetime(time);
				
				// 수행
				dc.ConnectDB(randomNumber, useFor);
				
				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		  }	
		};
		
		/* 데이터 전송 */
		Runnable runnableForSender = new Runnable() {
			public void run() {
				String useFor = "send_master"; // 사용 타입
				
				while(true) {
					// DB 연결
					DBConnection dc = new DBConnection();
					
					// 자동 close
					try(ServerSocket server = new ServerSocket()) {
						// 서버 초기화
						InetSocketAddress isa = new InetSocketAddress(9999);
						server.bind(isa);
						
						System.out.println("Server Initialize Complete");

						// Listen 대기
						Socket client = server.accept();
						client.setSoTimeout(2000); // 타임 아웃 설정
						System.out.println("Connection");
						
						// send, receiver 스트림 받아오기
						// 자동 close
						try(ObjectOutputStream sender = new ObjectOutputStream(client.getOutputStream());
								ObjectInputStream receiver = new ObjectInputStream(client.getInputStream());) {
							
							// 클라이언트로 부터 ID 값 받아오기
							int random_number_id = ((RandomNumber) receiver.readObject()).getRandom_number(); // 수신
							
							RandomNumber randomNumber = new RandomNumber();
							randomNumber.setRandom_number_id(random_number_id);
							
							// 넘겨 받은 random_number_id를 통해서 master에 있는 데이터를 가져온다.
							List<RandomNumber> randomNumberList = (List<java_program.RandomNumber>) dc.ConnectDB(randomNumber, useFor).get("resultList");
									//randomNumberDao.selectRandomNumberList(randomNumber);
							
							System.out.println("데이터 가져옴...");
							
							// 객체 전송시 직렬화 해서 전송
							RandomNumber temp = new RandomNumber();
							temp.setRandom_number_list(randomNumberList);
							
							sender.writeObject(temp); // 전송
							
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
