package sk409.youtube.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class ChannelStoreRequest {

    @NotNull
    @Size(min = 1, max = 256)
    @Getter
    @Setter
    private String name;

}