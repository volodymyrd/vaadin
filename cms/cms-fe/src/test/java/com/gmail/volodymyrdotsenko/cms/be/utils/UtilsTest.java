package com.gmail.volodymyrdotsenko.cms.be.utils;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void convertDate() {		
		System.out.println(Utils.convertToVtt(11.018321));
		System.out.println(Utils.dateToVtt(Utils.vttToDate(Utils.convertToVtt(11.018321))));
		
		System.out.println(Utils.dateToVtt(Utils.vttToDate("00:00:08.123")));
	}
}