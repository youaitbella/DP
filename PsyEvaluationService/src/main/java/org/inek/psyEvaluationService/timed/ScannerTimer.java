package org.inek.psyEvaluationService.timed;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import java.math.BigInteger;

@Singleton
@Startup
public class ScannerTimer {

    @Resource
    private TimerService _timerService;

    @PostConstruct
    public void initialize(){
        //ScheduleExpression expression = new ScheduleExpression();
        //expression.second("*/1").minute("*").hour("*");
        //_timerService.createCalendarTimer(expression);
    }

    @Timeout
    public void execute(){
        try {
            BigInteger sum = BigInteger.valueOf(1);
            for (int i = 2 ; i < 500000000 ; i++) {
                sum = sum.add(BigInteger.valueOf(i).multiply(BigInteger.valueOf(i)));
            }
            System.out.println(sum);
        }
        catch (Exception ey) {
            ey.printStackTrace();
        }
        //System.out.println("----Invoked: " + System.currentTimeMillis());
    }


    public void startTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.second("*/1").minute("*").hour("*");
        _timerService.createCalendarTimer(expression);
    }

    public void stopTimer() {
        for (Timer allTimer : _timerService.getAllTimers()) {
            allTimer.cancel();
        }

    }
}
