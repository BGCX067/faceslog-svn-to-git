package cn.hexiao.flog.faces.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class MonthConverter implements Converter {

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String month = arg2.toString();
		DateFormat df = new SimpleDateFormat("yyyyMM");
		//df.setLenient(false); //使用宽松的格式
		Date date = null;
		try {
			date = df.parse(month);
		} catch (ParseException e) {
			throw new ConverterException("月份参数不正确",e);
		}
		if(date != null) {
			return new SimpleDateFormat("yyyy年MM月").format(date);
		}
		return month;
	}

}
