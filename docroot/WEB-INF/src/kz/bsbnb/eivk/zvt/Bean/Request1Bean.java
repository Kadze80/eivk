package kz.bsbnb.eivk.zvt.Bean;

public class Request1Bean {
	private  String request1Num;//-- Учетный номер контракта
	private  String request1Date;//-- Дата контракта
	private  String request1BIN;//-- БИН юридического лица либо ИИН физического лица 
	private  String request1RezID;//-- Идентификационный номер нерезидента
	private  String request1RegNum;//-- Регистрационный номер заявления
	private  int err_code;//-- Код ошибки
	private  String err_msg;//-- Текст ошибки
	private  String extended_err_msg;//-- Расширенный текст ошибки
	
	
	public int getErr_code() {
		return err_code;
	}
	public void setErr_code(int err_code) {
		this.err_code = err_code;
	}
	public String getErr_msg() {
		return err_msg;
	}
	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}
	public String getExtended_err_msg() {
		return extended_err_msg;
	}
	public void setExtended_err_msg(String extended_err_msg) {
		this.extended_err_msg = extended_err_msg;
	}
	public String getRequest1Num() {
		return request1Num;
	}
	public void setRequest1Num(String request1Num) {
		this.request1Num = request1Num;
	}
	public String getRequest1Date() {
		return request1Date;
	}
	public void setRequest1Date(String request1Date) {
		this.request1Date = request1Date;
	}
	public String getRequest1BIN() {
		return request1BIN;
	}
	public void setRequest1BIN(String request1bin) {
		request1BIN = request1bin;
	}
	public String getRequest1RezID() {
		return request1RezID;
	}
	public void setRequest1RezID(String request1RezID) {
		this.request1RezID = request1RezID;
	}
	public String getRequest1RegNum() {
		return request1RegNum;
	}
	public void setRequest1RegNum(String request1RegNum) {
		this.request1RegNum = request1RegNum;
	}


}
