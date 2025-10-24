package com.giacomopillitteri.eventi.dto;

import lombok.Data;

@Data
public class RegistrazioneRequest {
    private String username;
    private String password;
}