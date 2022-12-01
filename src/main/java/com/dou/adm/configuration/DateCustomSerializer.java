package com.dou.adm.configuration;

import com.dou.adm.shared.CommonStrings;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCustomSerializer extends StdSerializer<Date> {

    public DateCustomSerializer() {
        super(Date.class);
    }

    protected DateCustomSerializer(Class<Date> t) {
        super(t);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        DateFormat df = new SimpleDateFormat(CommonStrings.DATE_FORMAT);
        String reportDate = df.format(value);
        gen.writeString(reportDate);
    }
}
