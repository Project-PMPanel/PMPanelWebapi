package project.daihao18.panel.common.configs;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 实现日期格式化方法1：
    // 覆盖 GsonHttpMessageConvertersConfiguration 中默认的 GsonHttpMessageConverter
    /*@Bean GsonHttpMessageConverter gsonHttpMessageConverter() { GsonHttpMessageConverter converter = new GsonHttpMessageConverter(); converter.setGson(new GsonBuilder().setDateFormat("yyyy/MM/dd").create()); return converter; }*/

    // 实现日期格式化方法2（更小的粒度）：
    // 覆盖 GsonHttpMessageConvertersConfiguration 中默认的 Gson（由 GsonAutoConfiguration 中注入）
    @Bean
    Gson gson() {
        Gson gson = new GsonBuilder()/*.serializeNulls(),加上会序列化为null的字段*/.setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        String datetime = json.getAsJsonPrimitive().getAsString();
                        return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                }).registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        String datetime = json.getAsJsonPrimitive().getAsString();
                        return LocalDate.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                }).create();
        return gson;
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //字符串转换器
        List<MediaType> listString = new ArrayList<MediaType>();
        //字符串的消息类型为text/plain
        listString.add(MediaType.TEXT_PLAIN);
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setSupportedMediaTypes(listString);

        converters.add(stringHttpMessageConverter);
    }


}