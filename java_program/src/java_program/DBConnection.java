package java_program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnection {

	private Connection conn;
	private Statement stmt;
	
	//private DBConnection() {
		// TODO Auto-generated constructor stub
	//};
	
	private static class SingleHolder {
		public static DBConnection single = new DBConnection();
	}
	
	public static DBConnection getInstance() {
		return SingleHolder.single;
	}
	
	public HashMap<String, Object> ConnectDB(RandomNumber randomNumber, String useFor) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		// db Ŀ�ؼ�
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String uri = "jdbc:mysql://127.0.0.1:3306/toma";
			String id = "toma";
			String pwd = "toma";
			
			conn = DriverManager.getConnection(uri, id, pwd);
			System.out.println("Database Connected...!");
			
			// ���� ��� �뵵�� ���� �б�
			if (useFor.equals("insert_master")) { 		// ����
				insertMasterData(randomNumber);
			} else if (useFor.equals("send_master")) {	// ����
				List<RandomNumber> resultList = selectMasterData(randomNumber);
				resultMap.put("resultList", resultList);
			} else if (useFor.equals("receiver_backup_count")) {  // ����
				int count = selectBackupDataCount();
				resultMap.put("resultCount", count);
			} else if (useFor.equals("receiver_backup_select")) {	// ID �� 
				int bakupId = selectBackupMaxId();
				resultMap.put("resultMaxId", bakupId);
			} else if (useFor.equals("receiver_backup_insert")) { // bakup db ���
				insertBackupData(randomNumber);
			}
			
			conn.close();
			//resultMap.put("result", "success"); 
			return resultMap;
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//resultMap.put("result", "fail");
		return resultMap;
	}

	private void insertMasterData(RandomNumber randomNumber) {
		// Master DB�� ����
		String sql = "insert into MASTER(random_number, registered_datetime) values (?,?);";
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, randomNumber.getRandom_number());
			pstmt.setTimestamp(2, randomNumber.getRegistered_datetime());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	private List<RandomNumber> selectMasterData(RandomNumber randomNumber) {
		// �Ѱ� ���� ID�� Master DB ��ȸ
		String sql = "select random_number_id, random_number, registered_datetime "
				+ "from MASTER where random_number_id > ?";
		PreparedStatement pstmt = null;
		
		List<RandomNumber> list = new ArrayList<RandomNumber>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, randomNumber.getRandom_number_id());
			ResultSet result = pstmt.executeQuery();
			
			while(result.next()) {
				RandomNumber ran = new RandomNumber();
				ran.setRandom_number_id(result.getInt("random_number_id"));
				ran.setRandom_number(result.getInt("random_number"));
				ran.setRegistered_datetime(result.getTimestamp("registered_datetime"));
				list.add(ran);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	private int selectBackupDataCount() {
		// Backup ���̺� ���� �����´�.
		String sql = "select COUNT(*) AS backup_count from BACKUP";
		PreparedStatement pstmt = null;
		
		int count = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet result = pstmt.executeQuery();
			
			while(result.next()) {
				RandomNumber ran = new RandomNumber();
				ran.setBackup_count(result.getInt("backup_count"));
				count = ran.getBackup_count();
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return count;
	}
	
	private int selectBackupMaxId() {
		// Backup ���̺��� ���� ������ Id�� �����´�.
		String sql = "select MAX(random_number_id) AS random_number_id from BACKUP";
		PreparedStatement pstmt = null;
		
		int backupId = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet result = pstmt.executeQuery();
			
			while(result.next()) {
				RandomNumber ran = new RandomNumber();
				ran.setRandom_number_id(result.getInt("random_number_id"));
				backupId = ran.getRandom_number_id();
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return backupId;
	}
	
	private void insertBackupData(RandomNumber randomNumber) {
		// Backup ���̺� ������ ����
		String sql = "insert into BACKUP(random_number_id, random_number, registered_datetime) values (?,?,?);";
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, randomNumber.getRandom_number_id());
			pstmt.setInt(2, randomNumber.getRandom_number());
			pstmt.setTimestamp(3, randomNumber.getRegistered_datetime());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
