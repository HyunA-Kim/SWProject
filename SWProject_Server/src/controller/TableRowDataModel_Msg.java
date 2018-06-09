package controller;

import javafx.beans.property.StringProperty;

public class TableRowDataModel_Msg {
	// ���� �޼��� ��
	// ���α׷���, ���̵�, �޼�������, �����ð�
	
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
