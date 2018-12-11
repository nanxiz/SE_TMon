package com.trafficmon;


import org.junit.Rule;
import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import java.math.BigDecimal;


public class SystemServiceTest {
    private final Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private PenaltiesService operationsTeam = context.mock(PenaltiesService.class);
    private SystemService systemService = new SystemService(operationsTeam);



    /**
     * If the account credit is insufficient
     * Will issue penalty notice
     */
    @Test
    public void checkPenaltyNoticeForInsufficientCredit() {
        BigDecimal charge = new BigDecimal(10000000);

        context.checking(new Expectations(){{
            exactly(1).of(operationsTeam).issuePenaltyNotice(vehicle, charge);
        }});

        systemService.chargeDeduction(vehicle, charge);
    }



    /**
     * If the vehicle is not previously registered
     * Will issue penalty notice
     **/
    @Test
    public void checkPenaltyNoticeForNotPreviouslyRegistered(){
        Vehicle vehicle = Vehicle.withRegistration("AAA XXX");
        BigDecimal charge = new BigDecimal(1);

        context.checking(new Expectations(){{
            exactly(1).of(operationsTeam).issuePenaltyNotice(vehicle, charge);
        }});

        systemService.chargeDeduction(vehicle, charge);
    }


    /**
     * Deduct charge from the account normally
     * Without issuing penalty notice
     */
    @Test
    public void checkChargeDeduction(){
        BigDecimal charge = new BigDecimal(1);

        context.checking(new Expectations(){{
            exactly(0).of(operationsTeam).issuePenaltyNotice(vehicle,charge);
        }});

        systemService.chargeDeduction(vehicle, charge);
    }



}