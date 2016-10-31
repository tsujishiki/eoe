package top.sdaily.web;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by soya on 2016/10/29.
 */
@ControllerAdvice
public class ExceptionController {

    private final Logger log = Logger.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(WebRequest request, Exception e){
        log.error("系统出现异常:",e);
        return e.toString();
    }

}
