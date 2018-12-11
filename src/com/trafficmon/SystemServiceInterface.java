package com.trafficmon;

import java.math.BigDecimal;

public interface SystemServiceInterface {
    //void systemBehaviors(Vehicle vehicle, BigDecimal charge);
    void issuePenalty(Vehicle vehicle, BigDecimal charge);

    void triggerInvestigation(Vehicle vehicle);

    void chargeDeduction(Vehicle vehicle, BigDecimal charge);// throws AccountNotRegisteredException, InsufficientCreditException;
}
