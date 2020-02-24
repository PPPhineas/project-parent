package com.hgt.project.common.util;

import lombok.Data;
import org.apache.commons.collections.MapUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
@Data
public class GridUtils {
	
	public static String formatContent(Column column, String content) {
		try {
			// 处理码表
			if (column.getCodeTable()!=null) {
				if (column.getCodeTable().containsKey(content)) {
					return MapUtils.getString(column.getCodeTable(), content);
				}
			}
			// 处理日期、数字的默认情况
			if ("date".equalsIgnoreCase(column.getType())&&column.getFormat()!=null&&!"".equals(column.getFormat())) {
				if (column.getOtype()!=null&&!"".equals(column.getOtype())) {
					if ("time_stamp_s".equals(column.getOtype())) {
						SimpleDateFormat sdf = new SimpleDateFormat(column.getFormat());
						Date date = new Date(Integer.parseInt(content)*1000);
						return sdf.format(date);
					} else if ("time_stamp_ms".equals(column.getOtype())) {
						SimpleDateFormat sdf = new SimpleDateFormat(column.getFormat());
						Date date = new Date(Integer.parseInt(content));
						return sdf.format(date);
					} else if ("string".equals(column.getOtype())) {
						if (column.getOriginFormat()!=null&&!"".equals(column.getOriginFormat())) {
							SimpleDateFormat originSdf= new SimpleDateFormat(column.getOriginFormat());
							SimpleDateFormat sdf = new SimpleDateFormat(column.getFormat());
							Date date = originSdf.parse(content);
							return sdf.format(date);
						}
					}
				}
			} else if ("number".equalsIgnoreCase(column.getType())&&!"".equals(column.getFormat())&& null != column.getFormat()) {
				DecimalFormat df = new DecimalFormat(column.getFormat());
				content = df.format(Double.parseDouble(content));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return content;
	}

}