package me.alexisdev.fineriomicroservice.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@ToString
public class ResponseToken implements Serializable {

    private String username;
    private ArrayList<String> roles = new ArrayList<>();
    private String token_type;
    private String access_token;
    private int expires_in;
    private String refresh_token;

}
