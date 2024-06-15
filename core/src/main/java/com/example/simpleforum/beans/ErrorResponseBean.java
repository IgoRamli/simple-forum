package com.example.simpleforum.beans;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class ErrorResponseBean implements Serializable {
    private Timestamp requestTimestamp;
    private Integer status;
    private String message;
    private String reason;

    public ErrorResponseBean(HttpServletRequest req, HttpStatus status, Exception ex) {
        this.requestTimestamp = new Timestamp(req.getSession().getLastAccessedTime());
        this.status = status.value();
        this.message = ex.getLocalizedMessage();
        this.reason = ex.getMessage();
    }
}
