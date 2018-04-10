package java_program;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class Sender {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final long timeInterval = 100;
		
		Random random = new Random();
		
		Runnable runnable = new Runnable() {
		public void run() {
			
			while(true) {
				// 현재 시간을 구한다.
				DBConnection dc = new DBConnection();
				
				LocalDateTime localDate = LocalDateTime.now();
				Timestamp time = Timestamp.valueOf(localDate);
				
				RandomNumber randomNumber = new RandomNumber();
				randomNumber.setRandom_number(random.nextInt());
				randomNumber.setRegistered_datetime(time);
				
				System.out.println(randomNumber.getRandom_number() + ", " +  randomNumber.getRegistered_datetime());
				
				System.out.println(dc.ConnectDB());
				
				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		  }	
		};
		
		Thread thread = new Thread(runnable);
		thread.start();
		
		//randomNumberDao.insertRandomNumber(randomNumber);
	}
	
	

}
