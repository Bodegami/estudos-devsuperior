package br.com.bodegami.dscatalog.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;

public record StandardError(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {

}
