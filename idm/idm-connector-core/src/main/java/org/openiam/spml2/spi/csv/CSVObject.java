package org.openiam.spml2.spi.csv;

public class CSVObject<T> {

	private String principal;
	private T object;

	public CSVObject() {

	}

	public CSVObject(String principal, T object) {
		super();
		this.principal = principal;
		this.object = object;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
}
