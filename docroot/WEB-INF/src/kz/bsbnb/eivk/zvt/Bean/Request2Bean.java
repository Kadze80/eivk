package kz.bsbnb.eivk.zvt.Bean;

public class Request2Bean {
	private int row;
	private String DECLARATION_NUMBER;//-- Номер заявления о ввозе товаров
	private String DECLARATION_DATE;// -- Дата заявления о ввозе товаров
	private String EISIGN_STR;//-- Признак экспорт/импорт
	private String INVOICE_NUMBER;//-- Номер счет-фактуры
	private String INVOICE_DATE;//-- Дата счет-фактуры
	private String COST_OF_GOODS;//-- Стоимость товара
	private String CURRENCY_CODE;//-- Трехзначный числовой код валюты
	private String DATE_OF_REGISTRATION;//-- Дата принятия на учет товара
	private String CONTRACT_NUMBER;//-- Номер контракта
	private String CONTRACT_DATE;//-- Дата контракта
	private String RESIDENT_BIN_OR_IIN;//-- Реквизиты резидента
	private String TAX_AUTH_DATE_STAMP;//-- Дата отметки налогового органа
	private String NON_RESIDENT_NAME;//-- Наименование нерезидента
	private String NON_RESIDENT_ID;//-- Идентификационный номер нерезидента
	private String NON_RESIDENT_COUNTRY_3N;//-- Код страны продавца
	
	public String getDECLARATION_NUMBER() {
		return DECLARATION_NUMBER;
	}
	public void setDECLARATION_NUMBER(String dECLARATION_NUMBER) {
		DECLARATION_NUMBER = dECLARATION_NUMBER;
	}
	public String getDECLARATION_DATE() {
		return DECLARATION_DATE;
	}
	public void setDECLARATION_DATE(String dECLARATION_DATE) {
		DECLARATION_DATE = dECLARATION_DATE;
	}
	public String getEISIGN_STR() {
		return EISIGN_STR;
	}
	public void setEISIGN_STR(String eISIGN_STR) {
		EISIGN_STR = eISIGN_STR;
	}
	public String getINVOICE_NUMBER() {
		return INVOICE_NUMBER;
	}
	public void setINVOICE_NUMBER(String iNVOICE_NUMBER) {
		INVOICE_NUMBER = iNVOICE_NUMBER;
	}
	public String getINVOICE_DATE() {
		return INVOICE_DATE;
	}
	public void setINVOICE_DATE(String iNVOICE_DATE) {
		INVOICE_DATE = iNVOICE_DATE;
	}
	public String getCOST_OF_GOODS() {
		return COST_OF_GOODS;
	}
	public void setCOST_OF_GOODS(String cOST_OF_GOODS) {
		COST_OF_GOODS = cOST_OF_GOODS;
	}
	public String getCURRENCY_CODE() {
		return CURRENCY_CODE;
	}
	public void setCURRENCY_CODE(String cURRENCY_CODE) {
		CURRENCY_CODE = cURRENCY_CODE;
	}
	public String getDATE_OF_REGISTRATION() {
		return DATE_OF_REGISTRATION;
	}
	public void setDATE_OF_REGISTRATION(String dATE_OF_REGISTRATION) {
		DATE_OF_REGISTRATION = dATE_OF_REGISTRATION;
	}
	public String getCONTRACT_NUMBER() {
		return CONTRACT_NUMBER;
	}
	public void setCONTRACT_NUMBER(String cONTRACT_NUMBER) {
		CONTRACT_NUMBER = cONTRACT_NUMBER;
	}
	public String getCONTRACT_DATE() {
		return CONTRACT_DATE;
	}
	public void setCONTRACT_DATE(String cONTRACT_DATE) {
		CONTRACT_DATE = cONTRACT_DATE;
	}
	public String getRESIDENT_BIN_OR_IIN() {
		return RESIDENT_BIN_OR_IIN;
	}
	public void setRESIDENT_BIN_OR_IIN(String rESIDENT_BIN_OR_IIN) {
		RESIDENT_BIN_OR_IIN = rESIDENT_BIN_OR_IIN;
	}
	public String getTAX_AUTH_DATE_STAMP() {
		return TAX_AUTH_DATE_STAMP;
	}
	public void setTAX_AUTH_DATE_STAMP(String tAX_AUTH_DATE_STAMP) {
		TAX_AUTH_DATE_STAMP = tAX_AUTH_DATE_STAMP;
	}
	public String getNON_RESIDENT_NAME() {
		return NON_RESIDENT_NAME;
	}
	public void setNON_RESIDENT_NAME(String nON_RESIDENT_NAME) {
		NON_RESIDENT_NAME = nON_RESIDENT_NAME;
	}
	public String getNON_RESIDENT_ID() {
		return NON_RESIDENT_ID;
	}
	public void setNON_RESIDENT_ID(String nON_RESIDENT_ID) {
		NON_RESIDENT_ID = nON_RESIDENT_ID;
	}
	public String getNON_RESIDENT_COUNTRY_3N() {
		return NON_RESIDENT_COUNTRY_3N;
	}
	public void setNON_RESIDENT_COUNTRY_3N(String nON_RESIDENT_COUNTRY_3N) {
		NON_RESIDENT_COUNTRY_3N = nON_RESIDENT_COUNTRY_3N;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	
	
}
