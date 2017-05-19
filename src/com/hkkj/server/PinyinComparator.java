package com.hkkj.server;

import java.util.Comparator;

import com.hkkj.csrx.domain.CityModel;

/**
 * 比较拼音是否一样
 * @author wgs
 * @date 20140421
 */
public class PinyinComparator implements Comparator<CityModel> {

	public int compare(CityModel o1, CityModel o2) {
		if (o1.getNameSort().equals("@")
				|| o2.getNameSort().equals("#")) {
			return -1;
		} else if (o1.getNameSort().equals("#")
				|| o2.getNameSort().equals("@")) {
			return 1;
		} else {
			return o1.getNameSort().compareTo(o2.getNameSort());
		}
	}

}
