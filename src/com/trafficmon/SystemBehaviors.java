package com.trafficmon;

import java.math.BigDecimal;

public class SystemBehaviors {
    private AccountsService accountsService;
    private PenaltiesService operationsTeam;
    public SystemBehaviors(){
        this.accountsService = RegisteredCustomerAccountsService.getInstance();
        this.operationsTeam = OperationsTeam.getInstance();

    }

    public void issuePenalty(Vehicle vehicle, BigDecimal charge){
        operationsTeam.issuePenaltyNotice(vehicle,charge);
    }
    public void triggerInvestigation(Vehicle vehicle){
        operationsTeam.triggerInvestigationInto(vehicle);

    }
    public void chargeDetuction(Vehicle vehicle, BigDecimal charge)
                throws AccountNotRegisteredException, InsufficientCreditException {
        accountsService.accountFor(vehicle).deduct(charge);
    }




}
