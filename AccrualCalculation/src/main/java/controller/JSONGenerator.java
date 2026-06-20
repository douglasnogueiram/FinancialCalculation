package controller;

import java.util.List;

import model.InterestAccrualRecord;

public class JSONGenerator {

    public static String generateAccrualJSON(List<InterestAccrualRecord> records) {
        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < records.size(); i++) {
            InterestAccrualRecord record = records.get(i);

            json.append("{");
            json.append("\"accrualDaySequence\":").append(record.getAccrualDaySequence()).append(",");
            json.append("\"accruedDate\":\"").append(record.getAccruedDate()).append("\",");
            json.append("\"accruedInterestAmount\":").append(record.getAccruedInterestAmount()).append(",");
            json.append("\"amortizationAmount\":").append(record.getAmortizationAmount()).append(",");
            json.append("\"outstandingBalance\":").append(record.getOutstandingBalance());
            json.append("}");

            if (i < records.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");
        return json.toString();
    }
}
