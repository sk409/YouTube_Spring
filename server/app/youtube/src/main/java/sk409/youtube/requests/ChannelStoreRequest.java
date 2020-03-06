package sk409.youtube.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class ChannelStoreRequest {

    @NotNull
    @Size(min=1,max=256)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}