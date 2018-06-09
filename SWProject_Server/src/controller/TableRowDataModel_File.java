package controller;

import javafx.beans.property.StringProperty;

public class TableRowDataModel_File {
	// 파일 목록 리스트 구조
	// 프로그램명, 아이디, 메세지, 전송하는데 걸리는 시간, 읽은사이즈/전체파일 사이즈, 전송날짜
	private StringProperty Program;
	private StringProperty ID;
	private StringProperty Msg;
	private StringProperty Time;
	private StringProperty Size;
	private StringProperty Day;
	
	public TableRowDataModel_File(StringProperty Program,StringProperty ID,StringProperty Msg,StringProperty Size, StringProperty Time,StringProperty Day) {
		this.Program=Program;
		this.ID=ID;
		this.Size=Size;
		this.Msg=Msg;
		this.Time=Time;
		this.Day=Day;
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
	public StringProperty SizeProeprty() {
		return Size;
	}
	public StringProperty DayProperty() {
		return Day;
	}
}
