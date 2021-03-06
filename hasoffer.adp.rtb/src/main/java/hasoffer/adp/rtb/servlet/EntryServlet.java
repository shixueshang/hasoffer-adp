package hasoffer.adp.rtb.servlet;

import hasoffer.adp.rtb.bidder.RTBServer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lihongde on 2016/12/12 17:45
 */
@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class EntryServlet extends HttpServlet{

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request,response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String data;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            StringBuilder sb = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            data = sb.toString();
            RTBServer server = new RTBServer(data, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
