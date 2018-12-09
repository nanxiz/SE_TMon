package com.trafficmon;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;



class NewRuleCalculatorTest {
    @Rule
    private CrossingEvent aCrossingEvent = new CrossingEvent();
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private OperationsTeam operationsTeam = context.mock(OperationsTeam.class);
    private Calculator newCalculator = new NewRuleCalculator();
    private CongestionChargeSystem congestionChargeSystem = new CongestionChargeSystem();
    //private List<ZoneBoundaryCrossing> eventLog = new ArrayList<>();


    //2pm before <4
    //2 hou <4
    //2 qian >4
    //2 hou >4

    @Test
    public void multipleEnterWithin4HourForMoreThan4Hours(){
        Vehicle vehicle=Vehicle.withRegistration("A123 XYZ");
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        aCrossingEvent.makeCrossingEvent(vehicle,"08:03:30","10:02:27",eventLog);
        //assertThat(Calc)




    }


}