package com.lnl.finance.bean;

import java.util.Map;

public class Item {
	public static final int ITEM = 0;
	public static final int SECTION = 1;

	public final int type;
	public final String text;
	public final Map<String, Object> financeItem;

	public int sectionPosition;
	public int listPosition;

	public Item(int type, String text,  Map<String, Object> financeItem) {
		this.type = type;
		this.text = text;
		this.financeItem = financeItem;
	}

	@Override
	public String toString() {
		return text;
	}
}
