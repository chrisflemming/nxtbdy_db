package tech.nxtbdy;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class NxtBdyConsumerServlet extends javax.servlet.http.HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream requestBodyStream = request.getInputStream();
        String requestBody = IOUtils.toString(requestBodyStream, "UTF-8");
        IOUtils.closeQuietly(requestBodyStream);

        JSONArray updatesArray = new JSONArray(requestBody);


        try {
            Connection connection = Utils.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement pStmt = connection.prepareStatement("SELECT VEHICLE_ID, ROUTE_ID, DIRECTION, SUBURB FROM BUS_LOCATIONS");
            ResultSet rs = pStmt.executeQuery();

            Map<String, String> existingVehicleToSuburb = new HashMap<>();

            while (rs.next()) {
                String vehicleId = rs.getString(1);
                String routeId = rs.getString(2);
                String direction = rs.getString(3);
                String suburb = rs.getString(4);

                existingVehicleToSuburb.put(vehicleId, suburb);
            }

            for (int i=0; i < updatesArray.length(); i++) {
                JSONObject updateObj = updatesArray.getJSONObject(i);
                String vehicleId = updateObj.getString("vehicleid");
                String routeId = updateObj.getString("routeid");
                String direction = updateObj.getString("direction");
                String longitude = updateObj.getString("longitude");
                String latitude = updateObj.getString("latitude");

                if (existingVehicleToSuburb.containsKey(vehicleId)) {
                    PreparedStatement pStmt2 = connection.prepareStatement("UPDATE BUS_LOCATIONS SET ROUTE_ID = ?, DIRECTION = ?, LATITUDE = ?, LONGITUDE = ?, GEOM = SDO_GEOMETRY(?, 4326) WHERE VEHICLE_ID = ?");
                    pStmt2.setString(1, routeId);
                    pStmt2.setString(2, direction);

                    //

                    pStmt2.setDouble(3, Double.parseDouble(latitude));
                    pStmt2.setDouble(4, Double.parseDouble(longitude));
                    String pointWKT = "POINT(" + longitude + " " + latitude + ")";
                    pStmt2.setString(5, pointWKT);
                    pStmt2.setString(6, vehicleId);

                    pStmt2.executeUpdate();
                } else {
                    PreparedStatement pStmt3 = connection.prepareStatement("INSERT INTO BUS_LOCATIONS (VEHICLE_ID, ROUTE_ID, DIRECTION, LATITUDE, LONGITUDE, GEOM) VALUES (?, ?, ?, ?, ?, SDO_GEOMETRY(?, 4326))");
                    pStmt3.setString(1, vehicleId);
                    pStmt3.setString(2, routeId);
                    pStmt3.setString(3, direction);

                    //

                    pStmt3.setDouble(4, Double.parseDouble(latitude));
                    pStmt3.setDouble(5, Double.parseDouble(longitude));
                    String pointWKT = "POINT(" + longitude + " " + latitude + ")";
                    pStmt3.setString(6, pointWKT);


                    pStmt3.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        OutputStream responseStream = response.getOutputStream();
        IOUtils.write("Thank you come again", responseStream, "UTF-8");
        IOUtils.closeQuietly(responseStream);
    }
}
