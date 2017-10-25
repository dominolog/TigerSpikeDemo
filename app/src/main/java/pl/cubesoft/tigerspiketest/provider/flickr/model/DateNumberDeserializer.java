package pl.cubesoft.tigerspiketest.provider.flickr.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class DateNumberDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return new Date(1000 * Long.parseLong(p.getValueAsString()));
    }
}
