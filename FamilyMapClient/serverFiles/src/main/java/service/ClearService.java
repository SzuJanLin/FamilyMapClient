package service;


import DAO.Database;
import responses.ClearResponse;

/**
 * Clear the database
 */
public class ClearService {
    public ClearService() {
    }

    /**
     * Clear the database
     */
    public ClearResponse clear() {


        ClearResponse rp = new ClearResponse();
        //call all the daos and it's clear functions
        try {
            clearInner();
            rp = new ClearResponse("Clear succeeded.", true);
        } catch (Exception e) {
            rp.setMessage(e.getMessage());
            rp.setSuccess(false);
            e.printStackTrace();
        }


        return rp;
    }

    private void clearInner() throws Exception {
        Database db = new Database();
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
        } catch (Exception e) {
            db.closeConnection(false);
        }
    }
}
