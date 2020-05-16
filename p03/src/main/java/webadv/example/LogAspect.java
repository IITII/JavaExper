package webadv.example;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import webadv.example.model.User;

/**
 * @author IITII
 */
@Aspect
@Configuration
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(public * webadv.example.controller.HomeController.check(..)) && args(user,result,..)")
    public void doBefore(User user, BindingResult result) throws Throwable {
        logger.info(String.format("Account:%s, login %s.", user.getAccount(),result.hasErrors()?"failed":"succeeded"));

    }
}
