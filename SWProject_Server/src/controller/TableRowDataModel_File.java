package controller;

import javafx.beans.property.StringProperty;

public class TableRowDataModel_File {
	// ���� ��� ����Ʈ ����
	// ���α׷���, ���̵�, �޼���, �����ϴµ� �ɸ��� �ð�, ����������/��ü���� ������, ���۳�¥
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
