package com.gmail.volodymyrdotsenko.cms.fe.vaadin.views.multimedia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.TextField;

public class TimeLabelTextField extends TextField implements Converter<String, Date> {

	private static final long serialVersionUID = 1L;

	private final String pattern = "HH:mm:ss.SSS";

	// private DateToLongConverter k;

	public TimeLabelTextField() {
	}

	public TimeLabelTextField(String caption) {
		super(caption);
		setConverter(this);
	}

	@Override
	public Date convertToModel(String value, Class<? extends Date> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		System.out.println("convertToModel");
		
		if (value == null || value.isEmpty()) {
			return null;
		}

		try {
			return new SimpleDateFormat(pattern).parse(value);
		} catch (ParseException e) {
			throw new ConversionException(e.getMessage());
		}
	}

	@Override
	public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		System.out.println("convertToPresentation");
		
		if (value == null) {
			return "";
		}

		return new SimpleDateFormat(pattern).format(value);
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

	@Override
	public Class<Date> getModelType() {
		return Date.class;
	}
}