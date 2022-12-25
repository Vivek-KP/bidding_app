package com.bidderApp.bidz.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionObject {
    public Integer statusCode;
    public String message;
    public long timeStamp;
}
