package model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import classes.WaterSourceReport;
import classes.WaterPurityReport;

public class ReportDataObject {

    private Set<String> reports = new HashSet<>();
    private Map<String, WaterSourceReport> sourceReportMap = new HashMap<>();
    private Map<String, WaterPurityReport> purityReportMap = new HashMap<>();
    private AtomicInteger numSourceReports = new AtomicInteger();
    private AtomicInteger numPurityReports = new AtomicInteger();
    private static final ReportDataObject INSTANCE = new ReportDataObject();

    /**
     * Private constructor for the ReportDataObject to implement
     * the Singleton pattern
     */
    private ReportDataObject() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated the"
                + "UserDataObject.  Please getInstance().");
        }
        DatabaseReference sourceReports = getSourceReports();
        DatabaseReference purityReports = getPurityReports();
        sourceReports.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,
                    String prevChildKey) {
                int newSize = numSourceReports.incrementAndGet();
                System.out.println("Added Source Report: "
                    + dataSnapshot.getKey() + ", count is " + newSize);
                updateReports(dataSnapshot.getKey(), false);
                updateSourceReports(dataSnapshot.getKey(),
                    dataSnapshot.getValue(WaterSourceReport.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,
                    String prevChildKey) {
                int size = numSourceReports.get()
                    + numPurityReports.get();
                System.out.println("Edited " + dataSnapshot.getKey()
                    + ", count is " + size);
                updateReports(dataSnapshot.getKey(), false);
                updateSourceReports(dataSnapshot.getKey(),
                    dataSnapshot.getValue(WaterSourceReport.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("The report "
                    + dataSnapshot.getKey() + " has been deleted");
                updateReportsOnDelete(dataSnapshot.getKey(), false);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot,
                String prevChildKey) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("FireBase error:"
                    + databaseError.getMessage());
            }
        });
        purityReports.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,
                    String prevChildKey) {
                int newSize = numPurityReports.incrementAndGet();
                System.out.println("Added Purity Report: "
                    + dataSnapshot.getKey() + ", count is " + newSize);
                updateReports(dataSnapshot.getKey(), true);
                updatePurityReports(dataSnapshot.getKey(),
                    dataSnapshot.getValue(WaterPurityReport.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,
                    String prevChildKey) {
                int size = numSourceReports.get()
                    + numPurityReports.get();
                System.out.println("Edited " + dataSnapshot.getKey()
                    + ", count is " + size);
                updateReports(dataSnapshot.getKey(), true);
                updatePurityReports(dataSnapshot.getKey(),
                    dataSnapshot.getValue(WaterPurityReport.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("The report "
                    + dataSnapshot.getKey() + " has been deleted");
                updateReportsOnDelete(dataSnapshot.getKey(), true);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot,
                String prevChildKey) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("FireBase error:"
                    + databaseError.getMessage());
            }
        });
    }

    /**
     * Returns the singleton instance
     * @return UserDataObject singleton
     */
    public static ReportDataObject getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a reference to the "sourceReports" key in the database
     * @return sourceReports database reference
     */
    private DatabaseReference getSourceReports() {
        return DataManager.getReference("/sourceReports");
    }

    /**
     * Returns a reference to the "purityReports" key in the database
     * @return purityReports database reference
     */
    private DatabaseReference getPurityReports() {
        return DataManager.getReference("/purityReports");
    }

    /**
     * Returns a reference to the "reports" key in the database
     * @return reports database reference
     */
    private DatabaseReference getReports() {
        return DataManager.getReference("/reports");
    }

    /**
     * Adds a source report to the fire-base database
     * @param  reportToAdd - report instance to add
     */
    public void addSourceReport(WaterSourceReport reportToAdd) {
        String reportId = UUID.randomUUID().toString().substring(0, 8);
        reportToAdd.setId(reportId);
        DatabaseReference report = getSourceReports().child("/" + reportId);
        report.setValue(reportToAdd);
    }

    /**
     * Adds a purity report to the fire-base database
     * @param  reportToAdd - report instance to add
     */
    public void addPurityReport(WaterPurityReport reportToAdd) {
        String reportId = UUID.randomUUID().toString().substring(0, 8);
        reportToAdd.setId(reportId);
        DatabaseReference report = getPurityReports().child("/" + reportId);
        report.setValue(reportToAdd);
    }

    /**
     * Converts a source report to a purity report.
     * @param reportToAdd WaterPurityReport instance to add.
     * @param reportId WaterSourceReport that is being converted
     * into a WaterPurityReport.
     */
    public void confirmPurityReport(WaterPurityReport reportToAdd,
            String reportId) {
        deleteSourceReport(reportId);
        DatabaseReference report = getPurityReports().child("/" + reportId);
        report.setValue(reportToAdd);
    }

    /**
     * Deletes a source report from the fire-base database
     * @param  reportId ID of the report to delete.
     */
    public void deleteSourceReport(String reportId) {
        DatabaseReference r = getSourceReports().child("/" + reportId);
        r.removeValue();
        DatabaseReference reports = getReports().child("/" + reportId);
        reports.removeValue();
    }

    /**
     * Deletes a purity report from the fire-base database
     * @param  reportId ID of the report to delete.
     */
    public void deletePurityReport(String reportId) {
        DatabaseReference r = getPurityReports().child("/" + reportId);
        r.removeValue();
        DatabaseReference reports = getReports().child("/" + reportId);
        reports.removeValue();
    }

    /**
     * Determines if the queried report is in the database
     * @param  reportId The report to query
     * @return          True if the report exists in the database.
     */
    public Boolean reportExists(String reportId) {
        return reports.contains(reportId);
    }

    /**
     * Determines if the queried report is a purity report or not.
     * Be sure to check if the report exists before using this method.
     *
     * @param  reportId The report to query
     * @return True if the report is purity report, false if it is a source.
     */
    public Boolean isPurityReport(String reportId) {
        return purityReportMap.containsKey(reportId);
    }

    /**
     * Returns the Report object mapped to the specified reportId.
     * A WaterSourceReport is returned because it represents a source report.
     *
     * @param  reportId The report key in the mapping
     * @return          WaterSourceReport object
     */
    public WaterSourceReport getSourceReport(String reportId) {
        return sourceReportMap.get(reportId);
    }

    /**
     * Returns all saved WaterSourceReport objects.
     *
     * @return     A map mapping String reportId and WaterSourceReport
     */
    public Map<String, WaterSourceReport> getAllSourceReports() {
        return sourceReportMap;
    }

    /**
     * Returns the Report object mapped to the specified reportId.
     * A WaterPurityReport is returned because it represents a purity report.
     *
     * @param  reportId The report key in the mapping
     * @return          WaterPurityReport object
     */
    public WaterPurityReport getPurityReport(String reportId) {
        return purityReportMap.get(reportId);
    }

    /**
     * Returns all saved WaterPurityReport objects.
     *
     * @return     A map mapping String reportId and WaterPurityReport
     */
    public Map<String, WaterPurityReport> getAllPurityReports() {
        return purityReportMap;
    }

    /**
     * Overwrites the existing mapping associated with the reportId key
     * @param  reportToAdd New Report object to add to the database
     * @param  reportId Key to map the Report with
     */
    public void editSourceReport(WaterSourceReport reportToAdd,
            String reportId) {
        deleteSourceReport(reportId);
        addSourceReport(reportToAdd);
    }

    /**
     * Overwrites the existing mapping associated with the reportId key
     * @param  reportToAdd New Report object to add to the database
     * @param  reportId Key to map the Report with
     */
    public void editPurityReport(WaterPurityReport reportToAdd,
            String reportId) {
        deletePurityReport(reportId);
        addPurityReport(reportToAdd);
    }

    /**
     * Helper method from an add callback in adding either type of Report
     * @param  reportId - key to update.
     * @param  purity - Determines whether the report is a purity report or not.
     */
    private void updateReports(String reportId, Boolean purity) {
        DatabaseReference reports = getReports();
        reports.child("/size").setValue(numPurityReports.get()
            + numSourceReports.get());
        reports.child("/" + reportId).setValue(purity);
        this.reports.add(reportId);
    }

    /**
     * Helper method from delete callback to update local storage.
     * @param  reportId The report ID that was deleted.
     * @param  isPurityReport If the report deleted was a purityReport.
     */
    private void updateReportsOnDelete(String reportId,
            boolean isPurityReport) {
        int newCount;
        if (isPurityReport) {
            purityReportMap.remove(reportId);
            newCount = numPurityReports.decrementAndGet()
                + numSourceReports.get();
        } else {
            sourceReportMap.remove(reportId);
            newCount = numSourceReports.decrementAndGet()
                + numPurityReports.get();
        }
        reports.remove(reportId);
        getReports().child("/size").setValue(newCount);
    }

    /**
     * Updates local storage when a WaterSourceReport is edited.
     * @param  reportId Key to map the Report object with
     * @param  r Report structure that FireBase returns
     */
    private void updateSourceReports(String reportId, WaterSourceReport r) {
        sourceReportMap.put(reportId, r);
    }

    /**
     * Updates local storage when a WaterPurityReport is edited.
     * @param  reportId Key to map the Report object with
     * @param  r Report object from FireBase
     */
    private void updatePurityReports(String reportId, WaterPurityReport r) {
        purityReportMap.put(reportId, r);
    }
}
