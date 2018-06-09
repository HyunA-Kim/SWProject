package controller;

import javafx.beans.property.StringProperty;

public class TableRowDataModel_Msg {
	// 받은 메세지 모델
	// 프로그램명, 아이디, 메세지내용, 받은시각
	
	private StringProperty Program;
	private StringProperty ID;
	private StringProperty Msg;
	private StringProperty Time;
	
	public TableRowDataModel_Msg(StringProperty Program, StringProperty ID, StringProperty Msg, StringProperty Time) {
		this.Program=Program;
		this.ID=ID;
		this.Msg=Msg;
		this.Time=Time;
	}
	
	public StringProperty ProgramProperty() {
		return Program;
	}
	public StringProperty IDProperty() {
		return ID;
	}
	public StringProperty MsgProperty() {
		return Msg;
	}
	public StringProperty TimeProperty() {
		return Time;
	}
}
