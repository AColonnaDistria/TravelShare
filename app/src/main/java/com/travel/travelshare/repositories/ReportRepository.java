package com.travel.travelshare.repositories;

import com.travel.travelshare.model.user.Report;

public class ReportRepository extends SimpleRepository<Report> {
    public ReportRepository() {
        super(Report.class, "travelshare_reports");
    }
}
