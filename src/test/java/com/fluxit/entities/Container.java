package com.fluxit.entities;

import java.io.Serializable;

public class Container implements Serializable {

	private static final long serialVersionUID = 1L;

	Long id;
	String unitNbr;
	String typeIso;
	String category;
	String tState;
	String position;
	String vState;
	String lineOp;
	String consignee;
	String consigneeName;
	String obActualVisit;
	String pod;
	String length;
	String height;
	String cargoWeight;
	String tareWeight;
	String weight;
	String containerClass;
	String bookingNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUnitNbr() {
		return unitNbr;
	}

	public void setUnitNbr(String unitNbr) {
		this.unitNbr = unitNbr;
	}

	public String getTypeIso() {
		return typeIso;
	}

	public void setTypeIso(String typeIso) {
		this.typeIso = typeIso;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String gettState() {
		return tState;
	}

	public void settState(String tState) {
		this.tState = tState;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getvState() {
		return vState;
	}

	public void setvState(String vState) {
		this.vState = vState;
	}

	public String getLineOp() {
		return lineOp;
	}

	public void setLineOp(String lineOp) {
		this.lineOp = lineOp;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getObActualVisit() {
		return obActualVisit;
	}

	public void setObActualVisit(String obActualVisit) {
		this.obActualVisit = obActualVisit;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getCargoWeight() {
		return cargoWeight;
	}

	public void setCargoWeight(String cargoWeight) {
		this.cargoWeight = cargoWeight;
	}

	public String getTareWeight() {
		return tareWeight;
	}

	public void setTareWeight(String tareWeight) {
		this.tareWeight = tareWeight;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getContainerClass() {
		return containerClass;
	}

	public void setContainerClass(String containerClass) {
		this.containerClass = containerClass;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

}
