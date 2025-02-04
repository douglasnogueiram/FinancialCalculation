package controller;


import java.util.List;

import model.AccrualFlow;

public class JSONGenerator {

    public static String generateAccrualFlowJSON(List<AccrualFlow> accrualFlows) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");  // Iniciar o array JSON

        for (int i = 0; i < accrualFlows.size(); i++) {
            AccrualFlow accrualFlow = accrualFlows.get(i);

            jsonBuilder.append("{");
            jsonBuilder.append("\"idAccruedDay\":").append(accrualFlow.getIdAccruedDay()).append(",");
            jsonBuilder.append("\"accruedDay\":\"").append(accrualFlow.getAccruedDay()).append("\",");
            jsonBuilder.append("\"dailyInterestAmount\":").append(accrualFlow.getDailyInterestAmount()).append(",");
            jsonBuilder.append("\"dailyAmmortizationAmount\":").append(accrualFlow.getDailyAmmortizationAmount()).append(",");
            jsonBuilder.append("\"dailyBalance\":").append(accrualFlow.getDailyBalance());

            jsonBuilder.append("}");

            // Adiciona uma vírgula entre os objetos, mas não no último
            if (i < accrualFlows.size() - 1) {
                jsonBuilder.append(",");
            }
        }

        jsonBuilder.append("]");  // Fechar o array JSON
        return jsonBuilder.toString();
    }
}

