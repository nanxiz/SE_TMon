package com.trafficmon;


import org.junit.Rule;
import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import java.math.BigDecimal;


public class SystemServiceTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private PenaltiesService operationsTeam = context.mock(PenaltiesService.class);

    private SystemService systemService = new SystemService(operationsTeam);
    private CongestionChargeSystem ccsystem = new CongestionChargeSystem();
    private final Vehicle vehicle1 = Vehicle.withRegistration("A123 XYZ");


    @Test
    /**
     * If the account credit is insufficient
     * Will issue penalty notice
     */
    public void checkPenaltyNoticeForInsufficientCredit() {
        BigDecimal charge = new BigDecimal(10000000);

        context.checking(new Expectations(){{
            exactly(1).of(operationsTeam).issuePenaltyNotice(vehicle1, charge);
        }});

        systemService.chargeDeduction(vehicle1, charge);
    }


    @Test
    /**
     * If the vehicle is not previously registered
     * Will issue penalty notice
     **/
    public void checkPenaltyNoticeForNotPreviouslyRegistered(){
        Vehicle vehicle = Vehicle.withRegistration("AAA XXX");
        BigDecimal charge = new BigDecimal(1);

        context.checking(new Expectations(){{
            exactly(1).of(operationsTeam).issuePenaltyNotice(vehicle, charge);
        }});

        systemService.chargeDeduction(vehicle, charge);
    }

    @Test
    /**
     * Deduct charge from the account normally
     * Without issuing penalty notice
     */
    public void checkChargeDeduction(){
        BigDecimal charge = new BigDecimal(1);

        context.checking(new Expectations(){{
            exactly(0).of(operationsTeam).issuePenaltyNotice(vehicle1 ,charge);
        }});

        systemService.chargeDeduction(vehicle1, charge);
    }



}