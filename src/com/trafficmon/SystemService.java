package com.trafficmon;

import java.math.BigDecimal;

public class SystemService implements SystemServiceInterface {
    private AccountsService accountsService;
    private PenaltiesService operationsTeam;
    public SystemService(){
        this.accountsService = RegisteredCustomerAccountsService.getInstance();
        this.operationsTeam = OperationsTeam.getInstance();
    }

    @Override
    public void issuePenalty(Vehicle vehicle, BigDecimal charge){
        operationsTeam.issuePenaltyNotice(vehicle,charge);
    }
    @Override
    public void triggerInvestigation(Vehicle vehicle){
        operationsTeam.triggerInvestigationInto(vehicle);
    }
    @Override
    public void chargeDetuction(Vehicle vehicle, BigDecimal charge){
               // throws AccountNotRegisteredException, InsufficientCreditException {
        try {accountsService.accountFor(vehicle).deduct(charge);}
        catch(AccountNotRegisteredException|InsufficientCreditException e){
            issuePenalty(vehicle,charge);
        }
    }









}