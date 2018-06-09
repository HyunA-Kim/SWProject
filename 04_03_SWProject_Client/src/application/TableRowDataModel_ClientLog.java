package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class TableRowDataModel_ClientLog {

	private IntegerProperty Index;			//순서
	private StringProperty Time;			//시간
	private StringProperty LogMsg;			//로그메세지
	
	public TableRowDataModel_ClientLog(IntegerProperty Index,StringProperty Time,StringProperty LogMsg) {
		this.Index=Index;
		this.Time=Time;
		this.LogMsg=LogMsg;
	}
	
	public IntegerProperty IndexProperty() {
		return Index;
	}//순서 받아오기
	public StringProperty TimeProperty() {
		return Time;
	}//시각 받아오기
	public StringProperty MsgProperty() {
		return LogMsg;
	}//메세지 받아오기
	
}//Client Logfile 함수 구조 끝
