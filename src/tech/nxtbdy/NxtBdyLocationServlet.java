package tech.nxtbdy;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NxtBdyLocationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream requestBodyStream = request.getInputStream();
        String requestBody = IOUtils.toString(requestBodyStream, "UTF-8");
        System.out.println(requestBody);
        IOUtils.closeQuietly(requestBodyStream);

        JSONObject requestJson = new JSONObject(requestBody);

        Double lat = requestJson.getDouble("lat");
        Double lon = requestJson.getDouble("lon");

        JSONArray retArray = new JSONArray();

        try {
            Connection connection = Utils.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("select VEHICLE_ID, ROUTE_ID, DIRECTION, SUBURB, SDO_GEOM.SDO_DISTANCE(geom, SDO_GEOMETRY(?, 4326), 100, 'unit=M') as distance FROM BUS_LOCATIONS ORDER BY distance ASC");

            String pointWKT = "POINT(" + lon.toString() + " " + lat.toString() + ")";
            pStmt.setString(1, pointWKT);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                String vehicleId = rs.getString(1);
                String routeId = rs.getString(2);
                String direction = rs.getString(3);
                String suburb = rs.getString(4);

                JSONObject busObject = new JSONObject();

                busObject.put("vehicle_id", vehicleId);
                busObject.put("route_id", routeId);
                busObject.put("direction", direction);
                busObject.put("suburb", suburb);

                retArray.put(busObject);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        OutputStream responseStream = response.getOutputStream();
        IOUtils.write(retArray.toString(), responseStream, "UTF-8");
        IOUtils.closeQuietly(responseStream);

    }
}
