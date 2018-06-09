package controller;


import javafx.beans.property.IntegerProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableRowDataModel {
	// Tina,Net-back-up 프로그램 클라이언트 접속 창 모델
	// 접속 순서, 아이디명, 패스워드, 호스트 아이피, 자동전송 여부
	
	private final IntegerProperty index;
	private final StringProperty ID;
	private final StringProperty PassWord;
	private final StringProperty IP;
	private StringProperty Select;
//	public CheckBoxTableCell Check=new CheckBoxTableCell();
	public TableRowDataModel(Integer index,String ID,String PassWord,String IP,String value) {
		this.index=new SimpleIntegerProperty(index);
		this.ID=new SimpleStringProperty(ID);
		this.PassWord=new SimpleStringProperty(PassWord);
		this.IP=new SimpleStringProperty(IP);
		this.Select=new SimpleStringProperty(value);
	}
	public void setCheck(String CheckInfo) {
		if(CheckInfo.equals("true")) {
			this.Select=new SimpleStringProperty("    √");
		}
		else {
			this.Select=new SimpleStringProperty(" ");
		}
	}

	public IntegerProperty indexProperty() {
		return index;
	}
	public StringProperty IDProperty() {
		return ID;
	}
	public StringProperty PassWordProperty() {
		return PassWord;
	}
	public StringProperty IPProperty() {
		return IP;
	}
	public StringProperty SelectProperty() {
		return Select;
	}


}
