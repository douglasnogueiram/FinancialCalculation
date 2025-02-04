package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class BigDecimalAdapter extends TypeAdapter<BigDecimal> {
    @Override
    public void write(JsonWriter jsonWriter, BigDecimal bigDecimal) throws IOException {
        jsonWriter.value(bigDecimal.setScale(10, RoundingMode.HALF_UP).toPlainString());  // Reduz precisão
    }

    @Override
    public BigDecimal read(JsonReader jsonReader) throws IOException {
        return new BigDecimal(jsonReader.nextString());
    }


}

