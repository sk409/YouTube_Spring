package sk409.youtube.services;

import com.google.gson.Gson;

import org.springframework.stereotype.Service;

@Service
public class JSONService {

    private Gson gson = new Gson();

    public String toJSON(Object src) {
        return gson.toJson(src);
    }

}