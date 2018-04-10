package java_program;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class RandomNumber implements Serializable {

	private Integer random_number_id;		// ?‹ë³„ì ID
	private Integer random_number;			// ?œ?¤ ë²ˆí˜¸
	private Timestamp registered_datetime;	// ?“±ë¡? ?¼?‹œ
	
	private List<RandomNumber> random_number_list; // ?œ?¤ ë²ˆí˜¸ ëª©ë¡
	
	public Integer getRandom_number_id() {
		return random_number_id;
	}
	public void setRandom_number_id(Integer random_number_id) {
		this.random_number_id = random_number_id;
	}
	public Integer getRandom_number() {
		return random_number;
	}
	public void setRandom_number(Integer random_number) {
		this.random_number = random_number;
	}
	public Timestamp getRegistered_datetime() {
		return registered_datetime;
	}
	public void setRegistered_datetime(Timestamp registered_datetime) {
		this.registered_datetime = registered_datetime;
	}
	public List<RandomNumber> getRandom_number_list() {
		return random_number_list;
	}
	public void setRandom_number_list(List<RandomNumber> random_number_list) {
		this.random_number_list = random_number_list;
	}
	
}
