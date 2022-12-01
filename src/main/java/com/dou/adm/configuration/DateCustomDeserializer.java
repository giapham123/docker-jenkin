package com.dou.adm.configuration;

import com.dou.adm.shared.CommonStrings;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCustomDeserializer extends StdDeserializer<Date> {

    public DateCustomDeserializer() {
        super(Date.class);
    }


    protected DateCustomDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        try {
            return new SimpleDateFormat(CommonStrings.DATE_FORMAT).parse(node.asText());
        } catch (ParseException e) {
            return null;
        }
    }
}
