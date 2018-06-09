package controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class TableRowDataModel_Log {
	// 서버 로그 구조 모델
	// 순서,받은시각,내용
	
	private IntegerProperty Index;
	private StringProperty Time;
	private StringProperty LogMsg;
	
	public TableRowDataModel_Log(IntegerProperty Index,StringProperty Time,StringProperty LogMsg) {
		this.Index=Index;
		this.Time=Time;
		this.LogMsg=LogMsg;
	}
	
	public IntegerProperty IndexProperty() {
		return Index;
	}
	public StringProperty TimeProperty() {
		return Time;
	}
	public StringProperty MsgProperty() {
		return LogMsg;
	}	
}
